package com.github.dragon925.androidlearning.news.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dragon925.androidlearning.R
import com.github.dragon925.androidlearning.common.data.service.DataLoadingServiceHelper
import com.github.dragon925.androidlearning.common.domain.Category
import com.github.dragon925.androidlearning.common.domain.Event
import com.github.dragon925.androidlearning.common.ui.readParcelableArrayList
import com.github.dragon925.androidlearning.databinding.FragmentNewsBinding
import com.github.dragon925.androidlearning.news.ui.activities.NewsDetailsActivity
import com.github.dragon925.androidlearning.news.ui.adapters.NewsListAdapter
import com.github.dragon925.androidlearning.news.ui.models.NewsItem
import com.github.dragon925.androidlearning.news.ui.utils.toNewsItem
import com.google.android.material.divider.MaterialDividerItemDecoration

class NewsFragment : Fragment() {

    companion object {
        private const val SAVED_EVENTS = "savedEvents"
        private const val SAVED_FILTERS = "savedFilters"
        private const val SAVED_CHOSEN_FILTERS = "savedChosenFilters"

        @JvmStatic
        fun newInstance() = NewsFragment()
    }

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private val newsAdapter = NewsListAdapter(::openNewsDetails)
    private val filters = mutableMapOf<Int, Boolean>()

    private val events = mutableListOf<NewsItem>()

    private val serviceHelper = DataLoadingServiceHelper(
        ::handleSuccessLoadData,
        ::handleErrorLoadData
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentNewsBinding.inflate(inflater, container, false)
        .also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val divider = MaterialDividerItemDecoration(
            requireContext(), LinearLayoutManager.VERTICAL
        ).apply {
            setDividerInsetStartResource(requireContext(), R.dimen.spacing_l)
            dividerColor = requireContext().getColor(R.color.cool_grey)
        }
        binding.rvNews.adapter = newsAdapter
        binding.rvNews.addItemDecoration(divider)

        if (savedInstanceState == null || savedInstanceState.isEmpty) {
            updateUI(true)
            serviceHelper.bindService(requireContext())
        }

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (serviceHelper.isLoading) return

        outState.putParcelableArrayList(SAVED_EVENTS, ArrayList(events))
        outState.putIntArray(SAVED_FILTERS, filters.keys.toIntArray())
        outState.putIntArray(
            SAVED_CHOSEN_FILTERS,
            filters.filter { it.value }.map { it.key }.toIntArray()
        )
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { state ->
            if (state.isEmpty) return

            val savedEvents = state.readParcelableArrayList<NewsItem>(SAVED_EVENTS)
                ?.toList() ?: emptyList()

            val savedFilters = state.getIntArray(SAVED_FILTERS) ?: intArrayOf()
            val savedChosenFilters = state.getIntArray(SAVED_CHOSEN_FILTERS)?.toSet() ?: emptySet()

            events.clear()
            events.addAll(savedEvents)
            filters.clear()
            savedFilters.forEach { filters[it] = savedChosenFilters.contains(it) }

            binding.piLoading.visibility = View.GONE
            binding.rvNews.visibility = View.VISIBLE
            updateNews()
        }
    }

    private fun handleSuccessLoadData(events: List<Event>, categories: List<Category>) {
        handleLoadedData(events.map { it.toNewsItem(requireContext()) }, categories)
    }

    private fun handleErrorLoadData() {
        Log.e("NewsFragment", "loadDataWithService-onError")
    }

    private fun handleLoadedData(news: List<NewsItem>, categories: List<Category>) {
        requireActivity().runOnUiThread {
            events.clear()
            events.addAll(news)
            filters.clear()
            categories.forEach { filters[it.id] = false }
            updateNews()
            updateUI(false)
        }
    }

    private fun openNewsDetails(newsItem: NewsItem) {
        val intent = Intent(requireContext(), NewsDetailsActivity::class.java).apply {
            putExtra(NewsDetailsActivity.EXTRA_NEWS_ID, newsItem.id)
            putExtra(NewsDetailsActivity.EXTRA_NEWS_TITLE, newsItem.title)
        }
        startActivity(intent)
    }

    private fun updateUI(showLoading: Boolean) {
        with(binding) {
            piLoading.visibility = if (showLoading) View.VISIBLE else View.GONE
            rvNews.visibility = if (showLoading) View.GONE else View.VISIBLE
        }
    }

    private fun updateNews() {
        val chosenFilters = filters.filter { it.value }.map { it.key }
        newsAdapter.submitList(events.filter { item ->
            chosenFilters.isEmpty() || item.categoryIds.any { chosenFilters.contains(it) }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        serviceHelper.unbindService(requireContext())
    }
}