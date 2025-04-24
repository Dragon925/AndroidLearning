package com.github.dragon925.androidlearning.news.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.github.dragon925.androidlearning.core.api.ui.ComponentViewModel
import com.github.dragon925.androidlearning.core.api.ui.MultiViewModelFactory
import com.github.dragon925.androidlearning.core.api.ui.UIState
import com.github.dragon925.androidlearning.core.api.ui.components.TopAppBar
import com.github.dragon925.androidlearning.core.api.ui.create
import com.github.dragon925.androidlearning.core.api.ui.createFactoryByViewModel
import com.github.dragon925.androidlearning.core.api.ui.theme.AppTheme
import com.github.dragon925.androidlearning.core.api.ui.theme.lightOliveGreen
import com.github.dragon925.androidlearning.core.api.ui.theme.macaroniAndCheese
import com.github.dragon925.androidlearning.news.R
import com.github.dragon925.androidlearning.news.di.NewsDeps
import com.github.dragon925.androidlearning.news.di.components.DaggerNewsListComponent
import com.github.dragon925.androidlearning.news.di.components.NewsListComponent
import com.github.dragon925.androidlearning.news.ui.activities.NewsDetailsActivity
import com.github.dragon925.androidlearning.news.ui.models.NewsItem
import com.github.dragon925.androidlearning.news.ui.models.NewsListUIState
import com.github.dragon925.androidlearning.news.ui.viewmodels.NewsViewModel
import com.github.dragon925.androidlearning.news.ui.viewmodels.UnreadNewsViewModel
import jakarta.inject.Inject

class NewsFragment : Fragment() {

    companion object {
        private const val SAVED_FILTERS = "savedFilters"
    }

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory

    private val newsComponentViewModel: ComponentViewModel<NewsListComponent> by activityViewModels {
        ComponentViewModel.createBy<NewsListComponent, NewsDeps> {
            DaggerNewsListComponent.builder().deps(this).build()
        }
    }
    private val unreadNewsViewModel: UnreadNewsViewModel by activityViewModels()
    private val newsViewModel: NewsViewModel by viewModels {
        createFactoryByViewModel { viewModelFactory.create<NewsViewModel>() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        newsComponentViewModel.component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            AppTheme {
                val newsState by newsViewModel.state.collectAsStateWithLifecycle(UIState())
                newsState.data?.let { data ->
                    val unread = data.newsList.count { it.id !in data.readIds }
                    unreadNewsViewModel.updateUnreadCount(unread)
                }
                NewsList(newsState)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    @Composable
    private fun NewsList(state: UIState<NewsListUIState, String> = UIState()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    stringResource(R.string.news),
                    actions = {
                        IconButton(
                            onClick = { openFilters() },
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_filter),
                                contentDescription = stringResource(R.string.filter)
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
            ) {
                if (state.isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = macaroniAndCheese,
                        trackColor = lightOliveGreen
                    )
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                        .padding(AppTheme.dimens.spacingXs),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.spacingXs)
                ) {
                    items(
                        items = state.data?.newsList ?: emptyList(),
                        key = { it.id }
                    ) { newsItem ->
                        NewsListItem(newsItem) { openNewsDetails(newsItem) }
                    }
                }
            }
        }
    }

    private fun openFilters() {
        val bundle = Bundle().apply {
            putStringArray(
                FilterFragment.CHOSEN_FILTERS,
                newsViewModel.currentFilters.toTypedArray()
            )
        }
        findNavController().navigate(R.id.action_newsFragment_to_filterFragment, bundle)
    }

    private fun openNewsDetails(newsItem: NewsItem) {
        newsViewModel.markAsRead(newsItem.id)
        findNavController().navigate(
            R.id.action_newsFragment_to_newsDetailsActivity,
            bundleOf(
                NewsDetailsActivity.EXTRA_NEWS_ID to newsItem.id,
                NewsDetailsActivity.EXTRA_NEWS_TITLE to newsItem.title
            )
        )
    }

    @Preview(showBackground = true)
    @Composable
    private fun NewsListPreview() {
        val listState = NewsListUIState(
            newsList = listOf(
                NewsItem(
                    id = "1",
                    title = "Breaking News",
                    description = "This is a breaking news item.",
                    date = "2022-01-01",
                ),
                NewsItem(
                    id = "2",
                    title = "Another News",
                    description = "This is another news item.",
                    date = "2022-01-02",
                )
            )
        )
        AppTheme {
            NewsList(UIState(isLoading = true, data = listState))
        }
    }
}