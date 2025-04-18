package com.github.dragon925.androidlearning.news.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dragon925.androidlearning.R
import com.github.dragon925.androidlearning.common.ui.ComponentViewModel
import com.github.dragon925.androidlearning.common.ui.MultiViewModelFactory
import com.github.dragon925.androidlearning.common.ui.UIState
import com.github.dragon925.androidlearning.common.ui.create
import com.github.dragon925.androidlearning.common.ui.createFactoryByViewModel
import com.github.dragon925.androidlearning.databinding.FragmentNewsBinding
import com.github.dragon925.androidlearning.news.di.components.NewsListComponent
import com.github.dragon925.androidlearning.news.ui.activities.NewsDetailsActivity
import com.github.dragon925.androidlearning.news.ui.adapters.NewsListAdapter
import com.github.dragon925.androidlearning.news.ui.models.NewsItem
import com.github.dragon925.androidlearning.news.ui.models.NewsListUIState
import com.github.dragon925.androidlearning.news.ui.viewmodels.NewsViewModel
import com.github.dragon925.androidlearning.news.ui.viewmodels.UnreadNewsViewModel
import com.google.android.material.divider.MaterialDividerItemDecoration
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import jakarta.inject.Inject

class NewsFragment : Fragment() {

    companion object {
        private const val SAVED_FILTERS = "savedFilters"

        @JvmStatic
        fun newInstance() = NewsFragment()
    }

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory

    private val newsComponentViewModel: ComponentViewModel<NewsListComponent> by activityViewModels {
        ComponentViewModel.createBy { newsListComponent().build() }
    }
    private val unreadNewsViewModel: UnreadNewsViewModel by activityViewModels()
    private val newsViewModel: NewsViewModel by viewModels {
        createFactoryByViewModel { viewModelFactory.create<NewsViewModel>() }
    }
    private val compositeDisposable = CompositeDisposable()

    private val newsAdapter = NewsListAdapter(::openNewsDetails)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        newsComponentViewModel.component.inject(this)
    }

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

        newsViewModel.state.observeOn(AndroidSchedulers.mainThread())
            .subscribe { state ->
                updateState(state)
                state.data?.let { data ->
                    val unread = data.newsList.count { it.id !in data.readIds }
                    unreadNewsViewModel.updateUnreadCount(unread)
                }
            }
            .also(compositeDisposable::add)

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.action_filter -> {
                    val bundle = Bundle().apply {
                        putStringArray(
                            FilterFragment.CHOSEN_FILTERS,
                            newsViewModel.currentFilters.toTypedArray()
                        )
                    }
                    findNavController().navigate(R.id.action_screen_news_to_filterFragment, bundle)
                    true
                }
                else -> {
                    false
                }
            }
        }

        setFragmentResultListener(FilterFragment.REQUEST_KEY) { _, bundle ->
            if (bundle.getInt(FilterFragment.RESULT_CODE) == FilterFragment.RESULT_OK) {
                val result = bundle.getStringArray(FilterFragment.RESULT_KEY)?.toList() ?: emptyList()
                newsViewModel.setFilters(result)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArray(SAVED_FILTERS, newsViewModel.currentFilters.toTypedArray())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { state ->
            if (state.isEmpty) return

            val savedFilters = state.getStringArray(SAVED_FILTERS)?.toList() ?: emptyList()
            newsViewModel.setFilters(savedFilters)
        }
    }

    private fun openNewsDetails(newsItem: NewsItem) {
        newsViewModel.markAsRead(newsItem.id)
        findNavController().navigate(
            R.id.action_screen_news_to_newsDetailsActivity,
            bundleOf(
                NewsDetailsActivity.EXTRA_NEWS_ID to newsItem.id,
                NewsDetailsActivity.EXTRA_NEWS_TITLE to newsItem.title
            )
        )
    }

    private fun updateState(state: UIState<NewsListUIState, String>) {
        with(binding) {
            piLoading.isVisible = state.isLoading
            rvNews.isGone = state.isLoading
        }

        state.data?.newsList?.let { news ->
            newsAdapter.submitList(news)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.clear()
    }
}