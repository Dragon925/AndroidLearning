package com.github.dragon925.androidlearning.help.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.github.dragon925.androidlearning.common.ui.UIState
import com.github.dragon925.androidlearning.databinding.FragmentHelpCategoriesBinding
import com.github.dragon925.androidlearning.help.ui.viewmodels.HelpCategoriesViewModel
import com.github.dragon925.androidlearning.help.ui.adapters.HelpCategoryListAdapter
import com.github.dragon925.androidlearning.help.ui.models.HelpCategoryUIState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable


class HelpCategoriesFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = HelpCategoriesFragment()
    }

    private var _binding: FragmentHelpCategoriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HelpCategoriesViewModel by viewModels { HelpCategoriesViewModel.Factory }
    private val compositeDisposable = CompositeDisposable()

    private val helpCategoriesAdapter = HelpCategoryListAdapter()

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

        viewModel.state.observeOn(AndroidSchedulers.mainThread())
            .subscribe(::updateState)
            .also(compositeDisposable::add)
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
        compositeDisposable.clear()
    }
}