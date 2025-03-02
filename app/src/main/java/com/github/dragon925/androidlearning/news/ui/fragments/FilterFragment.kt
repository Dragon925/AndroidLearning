package com.github.dragon925.androidlearning.news.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.github.dragon925.androidlearning.R
import com.github.dragon925.androidlearning.common.data.repositories.CommonCategoryRepository
import com.github.dragon925.androidlearning.common.data.service.DataLoadingServiceHelper
import com.github.dragon925.androidlearning.common.domain.Category
import com.github.dragon925.androidlearning.databinding.FragmentFilterBinding
import com.github.dragon925.androidlearning.news.ui.adapters.FilterListAdapter
import com.github.dragon925.androidlearning.news.ui.models.FilterItem

private const val CHOSEN_FILTERS = "chosenFilters"

class FilterFragment : Fragment() {
    companion object {
        const val TAG = "FilterFragment"
        const val REQUEST_KEY = "FilterFragment-Request"
        const val RESULT_CODE = "FilterFragment-ResultCode"
        const val RESULT_KEY = "FilterFragment-Result"
        const val SAVED_CHOSEN_FILTERS = "FilterFragment-savedChosenFilters"
        const val SAVED_FILTERS = "FilterFragment-savedFilters"

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
    private val filters = mutableMapOf<Int, Boolean>()
    private val categories = mutableListOf<Category>()

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private val filterListAdapter = FilterListAdapter(::updateFilter)

    private lateinit var serviceHelper: DataLoadingServiceHelper<Category>

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

        serviceHelper = DataLoadingServiceHelper(
            clazz = Category::class.java,
            onSuccess = {data -> handleLoadedData(data, chosenFilters) },
            onError = ::handleErrorLoadData,
            loader = { CommonCategoryRepository.getCategories(requireContext().assets) }
        )

        if (savedInstanceState == null || savedInstanceState.isEmpty) {
            updateUI(true)
            serviceHelper.bindService(requireContext())
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.action_apply_filter -> {
                    val chosenFilters = filters.filter { it.value }.map { it.key }.toIntArray()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (serviceHelper.isLoading) return

        outState.putParcelableArrayList(SAVED_FILTERS, ArrayList(categories))
        outState.putIntArray(
            SAVED_CHOSEN_FILTERS,
            filters.filter { it.value }.map { it.key }.toIntArray()
        )
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { state ->
            if (state.isEmpty) return

            val savedCategories = BundleCompat.getParcelableArrayList(
                savedInstanceState, SAVED_FILTERS, Category::class.java
            )?.toList() ?: emptyList()
            val savedChosenFilters = state.getIntArray(SAVED_CHOSEN_FILTERS) ?: intArrayOf()
            handleLoadedData(savedCategories, savedChosenFilters)
        }
    }

    private fun handleErrorLoadData() {
        Log.e("FilterFragment", "loadDataWithService-onError")
    }

    private fun handleLoadedData(categories: List<Category>, chosenFilters: IntArray) {
        requireActivity().runOnUiThread {
            this@FilterFragment.categories.clear()
            this@FilterFragment.categories.addAll(categories)
            categories.forEach { filters[it.id] = false }
            chosenFilters.forEach { filters[it] = true }
            updateFilters()
            updateUI(false)
        }
    }

    private fun updateUI(showLoading: Boolean) {
        with(binding) {
            piLoading.visibility = if (showLoading) View.VISIBLE else View.GONE
            rvFilters.visibility = if (showLoading) View.GONE else View.VISIBLE
            toolbar.menu.findItem(R.id.action_apply_filter).isEnabled = !showLoading
        }
    }

    private fun updateFilters() {
        filterListAdapter.submitList(
            categories.map { FilterItem(it, filters[it.id] ?: false) }
        )
    }

    private fun updateFilter(id: Int, isChecked: Boolean) {
        filters[id] = isChecked
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        serviceHelper.unbindService(requireContext())
    }
}