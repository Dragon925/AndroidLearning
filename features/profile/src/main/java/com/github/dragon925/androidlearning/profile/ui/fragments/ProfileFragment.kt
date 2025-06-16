package com.github.dragon925.androidlearning.profile.ui.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.TakePicturePreview
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil3.load
import coil3.request.Disposable
import com.github.dragon925.androidlearning.core.api.ui.ComponentViewModel
import com.github.dragon925.androidlearning.core.api.ui.SimpleItemDecoration
import com.github.dragon925.androidlearning.core.api.ui.UIState
import com.github.dragon925.androidlearning.core.api.ui.createFactoryByViewModel
import com.github.dragon925.androidlearning.core.api.ui.registerActionLauncher
import com.github.dragon925.androidlearning.profile.R
import com.github.dragon925.androidlearning.profile.databinding.FragmentProfileBinding
import com.github.dragon925.androidlearning.profile.di.DaggerProfileComponent
import com.github.dragon925.androidlearning.profile.di.ProfileComponent
import com.github.dragon925.androidlearning.profile.di.ProfileDeps
import com.github.dragon925.androidlearning.profile.ui.adapters.FriendsListAdapter
import com.github.dragon925.androidlearning.profile.ui.models.ProfileUIState
import com.github.dragon925.androidlearning.profile.ui.viewmodels.ProfileViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

private const val USER_ID = "userId"

class ProfileFragment : Fragment() {

    private var userId: String = ""

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    internal lateinit var viewModelFactory: ProfileViewModel.Factory

    @VisibleForTesting
    internal var componentBuilder = DaggerProfileComponent.builder()

    private val componentViewModel: ComponentViewModel<ProfileComponent> by viewModels {
        ComponentViewModel.createBy<ProfileComponent, ProfileDeps> {
            componentBuilder.deps(this).build()
        }
    }
    private val viewModel: ProfileViewModel by viewModels {
        createFactoryByViewModel { viewModelFactory.create(userId) }
    }
    private var hasCustomAvatar = false

    private val friendsAdapter = FriendsListAdapter()

    private lateinit var permission: ActivityResultLauncher<String>

    private lateinit var camera: ActivityResultLauncher<Void?>

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private lateinit var openGallery: ActivityResultLauncher<Intent>

    private var imageLoader: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(USER_ID, "")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        componentViewModel.component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentProfileBinding.inflate(inflater, container, false)
        .also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener(EditAvatarDialogFragment.REQUEST_KEY) { _, bundle ->
            handleEditAvatarDialogResult(bundle.getInt(EditAvatarDialogFragment.RESULT_TYPE))
        }

        permission = registerActionLauncher(RequestPermission()) { granted ->
            when {
                granted -> camera.launch(null)
                else -> {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.camera_permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        camera = registerActionLauncher(TakePicturePreview()) { bitmap ->
            bitmap?.let {
                imageLoader?.dispose()
                hasCustomAvatar = true
                binding.ivAvatar.setImageBitmap(it)
            }
        }

        pickMedia = registerActionLauncher(PickVisualMedia()) { uri ->
            uri?.let {
                imageLoader?.dispose()
                hasCustomAvatar = true
                binding.ivAvatar.setImageURI(it)
            }
        }

        openGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    try {
                        requireContext().contentResolver.openInputStream(uri).use { inputStream ->
                            val bitmap = inputStream?.let { BitmapFactory.decodeStream(it) }
                            imageLoader?.dispose()
                            hasCustomAvatar = true
                            binding.ivAvatar.setImageBitmap(bitmap)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        with(binding) {

            rvFriends.addItemDecoration(SimpleItemDecoration(requireContext()))
            rvFriends.adapter = friendsAdapter

            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_edit -> {
                        findNavController().navigate(R.id.action_profileFragment_to_editAvatarDialogFragment)
                        true
                    }
                    else -> false
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::updateState)
            }
        }
    }

    private fun handleEditAvatarDialogResult(resultCode: Int) {
        when (resultCode) {
            EditAvatarDialogFragment.RESULT_CODE_TAKE_PHOTO -> takePhoto()
            EditAvatarDialogFragment.RESULT_CODE_CHOOSE_PHOTO -> choosePhoto()
            EditAvatarDialogFragment.RESULT_CODE_DELETE_PHOTO -> deletePhoto()
            else -> error("Unknown resultCode: $resultCode")
        }
    }

    private fun choosePhoto() {
        if (PickVisualMedia.isPhotoPickerAvailable(requireContext())) {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        } else {
            openGallery.launch(Intent(Intent.ACTION_GET_CONTENT)
                .apply { type = "image/*" }
            )
        }
    }

    private fun takePhoto() {
        permission.launch(android.Manifest.permission.CAMERA)
    }

    private fun deletePhoto() {
        binding.ivAvatar.setImageResource(R.drawable.image_user)
    }

    private fun updateState(state: UIState<ProfileUIState, String>) {
        state.data?.let { data ->
            with(binding) {
                imageLoader = data.avatar.takeIf { !hasCustomAvatar }
                    ?.let { ivAvatar.load(it) }
                tvName.text = data.name
                tvBirthday.text = data.birthday
                tvFieldOfActivity.text = data.fieldOfActivity
                friendsAdapter.submitList(data.friends)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        parentFragmentManager.clearFragmentResultListener(EditAvatarDialogFragment.REQUEST_KEY)
        imageLoader?.dispose()
    }
}