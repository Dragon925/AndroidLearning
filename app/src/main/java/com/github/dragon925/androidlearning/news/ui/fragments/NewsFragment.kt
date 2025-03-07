package com.github.dragon925.androidlearning.news.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dragon925.androidlearning.R
import com.github.dragon925.androidlearning.common.data.repositories.CommonEventRepository
import com.github.dragon925.androidlearning.common.data.service.DataLoadingServiceHelper
import com.github.dragon925.androidlearning.common.domain.Event
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

        @JvmStatic
        fun newInstance() = NewsFragment()
    }

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private val newsAdapter = NewsListAdapter(::openNewsDetails)
    private val filters = mutableSetOf<Int>()

    private val events = mutableListOf<NewsItem>()

    private val serviceHelper = DataLoadingServiceHelper(
        clazz = Event::class.java,
        onSuccess = ::handleSuccessLoadData,
        onError = ::handleErrorLoadData,
        loader = { CommonEventRepository.getEvents(requireContext().assets) }
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
                    parentFragmentManager.commit {
                        add(
                            R.id.main_nav_container,
                            FilterFragment.newInstance(filters.toIntArray()),
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
            if (bundle.getInt(FilterFragment.RESULT_CODE) == FilterFragment.RESULT_OK) {
                val result = bundle.getIntArray(FilterFragment.RESULT_KEY)?.toList() ?: emptyList()
                filters.clear()
                filters.addAll(result)
                updateNews()
            }
            if (!serviceHelper.isDone) {
                serviceHelper.reload()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (serviceHelper.isLoading) return

        outState.putParcelableArrayList(SAVED_EVENTS, ArrayList(events))
        outState.putIntArray(SAVED_FILTERS, filters.toIntArray())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { state ->
            if (state.isEmpty) return

            val savedEvents = BundleCompat.getParcelableArrayList(
                state, SAVED_EVENTS, NewsItem::class.java
            )?.toList() ?: emptyList()

            val savedFilters = state.getIntArray(SAVED_FILTERS)?.toList() ?: emptyList()

            events.clear()
            events.addAll(savedEvents)

            filters.clear()
            filters.addAll(savedFilters)

            updateUI(false)
            updateNews()
        }
    }

    private fun handleSuccessLoadData(events: List<Event>) {
        handleLoadedData(events.map { it.toNewsItem(requireContext()) })
    }

    private fun handleErrorLoadData() {
        Log.e("NewsFragment", "loadDataWithService-onError")
    }

    private fun handleLoadedData(news: List<NewsItem>) {
        requireActivity().runOnUiThread {
            events.clear()
            events.addAll(news)
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
        newsAdapter.submitList(events.filter { item ->
            filters.isEmpty() || item.categoryIds.any { filters.contains(it) }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        serviceHelper.unbindService(requireContext())
    }
}