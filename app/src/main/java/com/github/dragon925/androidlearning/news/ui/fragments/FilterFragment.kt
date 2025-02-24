package com.github.dragon925.androidlearning.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.github.dragon925.androidlearning.R
import com.github.dragon925.androidlearning.common.data.repository.CommonCategoryRepository
import com.github.dragon925.androidlearning.databinding.FragmentFilterBinding
import com.github.dragon925.androidlearning.news.ui.adapters.FilterListAdapter
import com.github.dragon925.androidlearning.news.ui.models.FilterItem

private const val CHOSEN_FILTERS = "chosenFilters"

class FilterFragment : Fragment() {
    companion object {
        const val TAG = "FilterFragment"
        const val REQUEST_KEY = "FilterFragment-Request"
        const val RESULT_KEY = "FilterFragment-Result"

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

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

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

        val categories = CommonCategoryRepository.getCategories(requireContext().assets)

        categories.forEach { filters[it.id] = false }
        chosenFilters.forEach { filters[it] = true }

        filterListAdapter.submitList(
            categories.map { FilterItem(it, filters[it.id] ?: false) }
        )

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.action_apply_filter -> {
                    val chosenFilters = filters.entries.filter { it.value }
                        .map { it.key }
                        .toIntArray()
                    setFragmentResult(
                        REQUEST_KEY,
                        bundleOf(RESULT_KEY to chosenFilters)
                    )
                    parentFragmentManager.popBackStack()
                    true
                }
                else -> false
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun updateFilter(id: Int, isChecked: Boolean) {
        filters[id] = isChecked
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}