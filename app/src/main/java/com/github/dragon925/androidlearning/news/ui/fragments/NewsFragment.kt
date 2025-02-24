package com.github.dragon925.androidlearning.news.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dragon925.androidlearning.R
import com.github.dragon925.androidlearning.common.data.repository.CommonCategoryRepository
import com.github.dragon925.androidlearning.databinding.FragmentNewsBinding
import com.github.dragon925.androidlearning.news.data.repositories.EventRepository
import com.github.dragon925.androidlearning.news.ui.activities.NewsDetailsActivity
import com.github.dragon925.androidlearning.news.ui.adapters.NewsListAdapter
import com.github.dragon925.androidlearning.news.ui.models.NewsItem
import com.github.dragon925.androidlearning.news.ui.utils.toNewsItem
import com.google.android.material.divider.MaterialDividerItemDecoration

class NewsFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = NewsFragment()
    }

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private val newsAdapter = NewsListAdapter(::openNewsDetails)
    private val filters = mutableMapOf<Int, Boolean>()

    private val events = mutableListOf<NewsItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentNewsBinding.inflate(inflater, container, false)
        .also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CommonCategoryRepository.getCategories(requireContext().assets).forEach { filters[it.id] = false }

        val divider = MaterialDividerItemDecoration(
            requireContext(), LinearLayoutManager.VERTICAL
        ).apply {
            setDividerInsetStartResource(requireContext(), R.dimen.spacing_l)
            dividerColor = requireContext().getColor(R.color.cool_grey)
        }
        binding.rvNews.adapter = newsAdapter
        binding.rvNews.addItemDecoration(divider)

        EventRepository.getEvents(requireContext().assets)
            .map { it.toNewsItem(requireContext()) }
            .forEach { events.add(it) }

        updateNews()

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.action_filter -> {
                    val chosenFilters = filters.filter { it.value }.map { it.key }.toIntArray()
                    parentFragmentManager.commit {
                        add(
                            R.id.main_nav_container,
                            FilterFragment.newInstance(chosenFilters),
                            FilterFragment.TAG
                        )
                        addToBackStack(FilterFragment.TAG)
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }

        setFragmentResultListener(FilterFragment.REQUEST_KEY) { _, bundle ->
            val result = bundle.getIntArray(FilterFragment.RESULT_KEY) ?: intArrayOf()
            filters.keys.forEach { filters[it] = result.contains(it) }
            updateNews()
        }
    }

    private fun openNewsDetails(newsItem: NewsItem) {
        val intent = Intent(requireContext(), NewsDetailsActivity::class.java).apply {
            putExtra(NewsDetailsActivity.EXTRA_NEWS_ID, newsItem.id)
            putExtra(NewsDetailsActivity.EXTRA_NEWS_TITLE, newsItem.title)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateNews() {
        val chosenFilters = filters.filter { it.value }.map { it.key }
        newsAdapter.submitList(events.filter { item ->
            chosenFilters.isEmpty() || item.categoryIds.any { chosenFilters.contains(it) }
        })
    }
}