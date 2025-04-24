package com.github.dragon925.androidlearning.help.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.github.dragon925.androidlearning.core.api.ui.ComponentViewModel
import com.github.dragon925.androidlearning.core.api.ui.MultiViewModelFactory
import com.github.dragon925.androidlearning.core.api.ui.UIState
import com.github.dragon925.androidlearning.core.api.ui.create
import com.github.dragon925.androidlearning.core.api.ui.createFactoryByViewModel
import com.github.dragon925.androidlearning.help.databinding.FragmentHelpCategoriesBinding
import com.github.dragon925.androidlearning.help.di.DaggerHelpComponent
import com.github.dragon925.androidlearning.help.di.HelpComponent
import com.github.dragon925.androidlearning.help.di.HelpDeps
import com.github.dragon925.androidlearning.help.ui.viewmodels.HelpCategoriesViewModel
import com.github.dragon925.androidlearning.help.ui.adapters.HelpCategoryListAdapter
import com.github.dragon925.androidlearning.help.ui.models.HelpCategoryUIState
import jakarta.inject.Inject
import kotlinx.coroutines.launch


class HelpCategoriesFragment : Fragment() {

    private var _binding: FragmentHelpCategoriesBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory

    private val helpComponentViewModel: ComponentViewModel<HelpComponent> by viewModels {
        ComponentViewModel.createBy<HelpComponent, HelpDeps> {
            DaggerHelpComponent.builder().deps(this).build()
        }
    }
    private val viewModel: HelpCategoriesViewModel by viewModels {
        createFactoryByViewModel { viewModelFactory.create<HelpCategoriesViewModel>() }
    }

    private val helpCategoriesAdapter = HelpCategoryListAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        helpComponentViewModel.component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHelpCategoriesBinding.inflate(inflater, container, false)
        .also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.rvHelpCategories) {
            adapter = helpCategoriesAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::updateState)
            }
        }
    }

    private fun updateState(state: UIState<HelpCategoryUIState, String>) {
        with(binding) {
            piLoading.isVisible = state.isLoading
            rvHelpCategories.isGone = state.isLoading
        }

        state.data?.helpCategories?.let { helpCategoriesAdapter.submitList(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}