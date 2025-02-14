package com.github.dragon925.androidlearning.profile.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.github.dragon925.androidlearning.R
import com.github.dragon925.androidlearning.databinding.FragmentProfileBinding
import com.github.dragon925.androidlearning.profile.ui.adapters.FriendsListAdapter
import com.github.dragon925.androidlearning.common.ui.SimpleItemDecoration
import com.github.dragon925.androidlearning.profile.ui.models.FriendItem

private const val USER_ID = "userId"

class ProfileFragment : Fragment() {

    companion object {

        @JvmStatic
        fun newInstance(userId: Int) = ProfileFragment().apply {
            arguments = Bundle().apply {
                putInt(USER_ID, userId)
            }
        }
    }

    private var userId: Int? = null

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val friendsAdapter = FriendsListAdapter()

    private val permission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
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

    private val camera = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let { binding.ivAvatar.setImageBitmap(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getInt(USER_ID)
        }
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

        with(binding) {

            rvFriends.addItemDecoration(SimpleItemDecoration(requireContext()))
            rvFriends.adapter = friendsAdapter

            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_edit -> {
                        EditAvatarDialogFragment()
                            .show(parentFragmentManager, EditAvatarDialogFragment.TAG)
                        true
                    }
                    else -> false
                }
            }
        }

        initSampleData()
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
        Log.d("ProfileFragment", "choosePhoto")
    }

    private fun takePhoto() {
        permission.launch(android.Manifest.permission.CAMERA)
    }

    private fun deletePhoto() {
        binding.ivAvatar.setImageResource(R.drawable.image_user)
    }

    private fun initSampleData() {
        with(binding) {
            ivAvatar.setImageResource(R.drawable.image_man)
            tvName.text = "Константинов Денис"
            tvBirthday.text = "01 февраля 1980"
            tvFieldOfActivity.text = "Хирургия, травматология"
            friendsAdapter.submitList(
                listOf(
                    FriendItem(1, R.drawable.avatar_1, "Дмитрий Валерьевич"),
                    FriendItem(2, R.drawable.avatar_2, "Евгений Александров"),
                    FriendItem(3, R.drawable.avatar_3, "Виктор Кузнецов"),
                ),
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        parentFragmentManager.clearFragmentResultListener(EditAvatarDialogFragment.REQUEST_KEY)
    }
}