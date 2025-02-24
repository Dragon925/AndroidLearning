package com.github.dragon925.androidlearning.help.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.github.dragon925.androidlearning.common.domain.Category
import com.github.dragon925.androidlearning.databinding.FragmentHelpCategoriesBinding
import com.github.dragon925.androidlearning.help.data.HelpRepository
import com.github.dragon925.androidlearning.help.ui.adapters.HelpCategoryListAdapter
import com.github.dragon925.androidlearning.help.ui.utils.toHelpCategoryItem


class HelpCategoriesFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = HelpCategoriesFragment()
    }

    private var _binding: FragmentHelpCategoriesBinding? = null
    private val binding get() = _binding!!

    private val categories = mutableListOf<Category>()

    private val helpCategoriesAdapter = HelpCategoryListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHelpCategoriesBinding.inflate(inflater, container, false)
        .also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        HelpRepository.getCategories(requireContext().assets).forEach { categories.add(it) }

        with(binding.rvHelpCategories) {
            adapter = helpCategoriesAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
        helpCategoriesAdapter.submitList(categories.map { it.toHelpCategoryItem(requireContext()) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}