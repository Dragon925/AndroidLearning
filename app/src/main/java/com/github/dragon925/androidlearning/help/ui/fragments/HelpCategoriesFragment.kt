package com.github.dragon925.androidlearning.help.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.github.dragon925.androidlearning.common.data.service.DataLoadingServiceHelper
import com.github.dragon925.androidlearning.common.domain.Category
import com.github.dragon925.androidlearning.common.ui.readParcelableArrayList
import com.github.dragon925.androidlearning.databinding.FragmentHelpCategoriesBinding
import com.github.dragon925.androidlearning.help.ui.adapters.HelpCategoryListAdapter
import com.github.dragon925.androidlearning.help.ui.utils.toHelpCategoryItem


class HelpCategoriesFragment : Fragment() {

    companion object {
        private const val SAVED_CATEGORIES = "HelpCategoriesFragment-savedCategories"

        @JvmStatic
        fun newInstance() = HelpCategoriesFragment()
    }

    private var _binding: FragmentHelpCategoriesBinding? = null
    private val binding get() = _binding!!

    private val categories = mutableListOf<Category>()

    private val helpCategoriesAdapter = HelpCategoryListAdapter()

    private val serviceHelper = DataLoadingServiceHelper(
        { _, categories -> handleLoadedData(categories) },
        ::handleErrorLoadData,
    )

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

        if (savedInstanceState == null || savedInstanceState.isEmpty) {
            updateUI(true)
            serviceHelper.bindService(requireContext())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (serviceHelper.isLoading) return

        outState.putParcelableArrayList(SAVED_CATEGORIES, ArrayList(categories))
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { state ->
            if (state.isEmpty) return

            val savedCategories = state.readParcelableArrayList<Category>(SAVED_CATEGORIES)
                ?.toList()
                ?: emptyList()
            handleLoadedData(savedCategories)
        }
    }

    private fun handleErrorLoadData() {
        Log.d("HelpCategoriesFragment", "loadDataWithService-onError")
    }

    private fun handleLoadedData(data: List<Category>) {
        requireActivity().runOnUiThread {
            categories.clear()
            categories.addAll(data)
            updateCategories()
            updateUI(false)
        }
    }

    private fun updateUI(showLoading: Boolean) {
        with(binding) {
            piLoading.visibility = if (showLoading) View.VISIBLE else View.GONE
            rvHelpCategories.visibility = if (showLoading) View.GONE else View.VISIBLE
        }
    }

    private fun updateCategories() {
        helpCategoriesAdapter.submitList(categories.map { it.toHelpCategoryItem(requireContext()) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        serviceHelper.unbindService(requireContext())
    }
}