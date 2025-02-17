package com.github.dragon925.androidlearning.help.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.github.dragon925.androidlearning.R
import com.github.dragon925.androidlearning.databinding.FragmentHelpCategoriesBinding
import com.github.dragon925.androidlearning.help.ui.adapters.HelpCategoryListAdapter
import com.github.dragon925.androidlearning.help.ui.models.HelpCategoryItem


class HelpCategoriesFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = HelpCategoriesFragment()
    }

    private var _binding: FragmentHelpCategoriesBinding? = null
    private val binding get() = _binding!!

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
        helpCategoriesAdapter.submitList(generateSampleCategoriesData())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun generateSampleCategoriesData(): List<HelpCategoryItem> = listOf(
        HelpCategoryItem(
            1,
            R.drawable.icon_kids,
            resources.getString(R.string.help_category_kids),
        ),
        HelpCategoryItem(
            2,
            R.drawable.icon_adult,
            resources.getString(R.string.help_category_adult),
        ),
        HelpCategoryItem(
            3,
            R.drawable.icon_elderly,
            resources.getString(R.string.help_category_elderly),
        ),
        HelpCategoryItem(
            4,
            R.drawable.icon_animals,
            resources.getString(R.string.help_category_animals),
        ),
        HelpCategoryItem(
            5,
            R.drawable.icon_event,
            resources.getString(R.string.help_category_event),
        )
    )
}