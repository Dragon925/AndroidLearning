package com.github.dragon925.androidlearning.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.github.dragon925.androidlearning.R
import com.github.dragon925.androidlearning.common.ui.UIState
import com.github.dragon925.androidlearning.databinding.FragmentFilterBinding
import com.github.dragon925.androidlearning.news.ui.adapters.FilterListAdapter
import com.github.dragon925.androidlearning.news.ui.models.FilterUIState
import com.github.dragon925.androidlearning.news.ui.viewmodels.FilterViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable

private const val CHOSEN_FILTERS = "chosenFilters"

class FilterFragment : Fragment() {
    companion object {
        const val TAG = "FilterFragment"
        const val REQUEST_KEY = "FilterFragment-Request"
        const val RESULT_CODE = "FilterFragment-ResultCode"
        const val RESULT_KEY = "FilterFragment-Result"

        const val RESULT_OK = 0
        const val RESULT_CANCEL = -1

        @JvmStatic
        fun newInstance(chosenFilters: IntArray = intArrayOf()) =
            FilterFragment().apply {
                arguments = Bundle().apply {
                    putIntArray(CHOSEN_FILTERS, chosenFilters)
                }
            }
    }

    private var chosenFilters: IntArray = intArrayOf()

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private val filterViewModel: FilterViewModel by viewModels { FilterViewModel.Factory }
    private val compositeDisposable = CompositeDisposable()

    private val filterListAdapter = FilterListAdapter(::updateFilter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chosenFilters = it.getIntArray(CHOSEN_FILTERS) ?: intArrayOf()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFilterBinding.inflate(inflater, container, false)
        .also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFilters.adapter = filterListAdapter

        filterViewModel.state.observeOn(AndroidSchedulers.mainThread())
            .subscribe(::updateState)
            .also(compositeDisposable::add)

        filterViewModel.checkCategory(*chosenFilters, isChecked = true)

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.action_apply_filter -> {
                    val chosenFilters = filterViewModel.currentChosenCategories.toIntArray()
                    setFragmentResult(
                        REQUEST_KEY,
                        bundleOf(
                            RESULT_CODE to RESULT_OK,
                            RESULT_KEY to chosenFilters
                        )
                    )
                    parentFragmentManager.popBackStack()
                    true
                }
                else -> false
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            setFragmentResult(REQUEST_KEY, bundleOf(RESULT_CODE to RESULT_CANCEL))
            parentFragmentManager.popBackStack()
        }
    }

    private fun updateState(state: UIState<FilterUIState, String>) {
        with(binding) {
            piLoading.isVisible = state.isLoading
            rvFilters.isGone = state.isLoading
            toolbar.menu.findItem(R.id.action_apply_filter).isEnabled = !state.isLoading
        }

        state.data?.filters?.let { filterListAdapter.submitList(it) }
    }

    private fun updateFilter(id: Int, isChecked: Boolean) {
        filterViewModel.checkCategory(id, isChecked = isChecked)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.clear()
    }
}