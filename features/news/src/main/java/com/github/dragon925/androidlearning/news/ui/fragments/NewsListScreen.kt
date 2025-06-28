package com.github.dragon925.androidlearning.news.ui.fragments

import android.os.Bundle
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.dragon925.androidlearning.core.api.ui.UIState
import com.github.dragon925.androidlearning.core.api.ui.components.TopAppBar
import com.github.dragon925.androidlearning.core.api.ui.theme.AppTheme
import com.github.dragon925.androidlearning.core.api.ui.theme.lightOliveGreen
import com.github.dragon925.androidlearning.core.api.ui.theme.macaroniAndCheese
import com.github.dragon925.androidlearning.news.R
import com.github.dragon925.androidlearning.news.ui.activities.NewsDetailsActivity
import com.github.dragon925.androidlearning.news.ui.models.NewsItem
import com.github.dragon925.androidlearning.news.ui.models.NewsListUIState
import com.github.dragon925.androidlearning.news.ui.viewmodels.NewsViewModel
import com.github.dragon925.androidlearning.news.ui.viewmodels.UnreadNewsViewModel
import kotlinx.collections.immutable.persistentListOf

internal const val TAG_LOADING_INDICATOR = "LoadingIndicator"
internal const val TAG_NEWS_ITEM = "NewsItem"

@Composable
internal fun NewsListScreen(
    newsViewModel: NewsViewModel = viewModel(),
    unreadNewsViewModel: UnreadNewsViewModel = viewModel(),
    navController: NavController = rememberNavController()
) {
    val state by newsViewModel.state.collectAsStateWithLifecycle(UIState())
    state.data?.let { data ->
        val unread = data.newsList.count { it.id !in data.readIds }
        unreadNewsViewModel.updateUnreadCount(unread)
    }

    NewsList(
        state = state,
        onOpenFilters = { openFilters(navController, newsViewModel) },
        onOpenDetails = { openNewsDetails(it, navController, newsViewModel) }
    )
}

@Composable
private fun NewsList(
    state: UIState<NewsListUIState, String> = UIState(),
    onOpenFilters: () -> Unit = {},
    onOpenDetails: (NewsItem) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                stringResource(R.string.news),
                actions = {
                    IconButton(
                        onClick = onOpenFilters,
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
                    modifier = Modifier.fillMaxWidth()
                        .testTag(TAG_LOADING_INDICATOR),
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
                    NewsListItem(
                        newsItem,
                        Modifier.testTag(TAG_NEWS_ITEM)
                    ) {
                        onOpenDetails(newsItem)
                    }
                }
            }
        }
    }
}

private fun openFilters(navController: NavController, newsViewModel: NewsViewModel) {
    val bundle = Bundle().apply {
        putStringArray(
            FilterFragment.CHOSEN_FILTERS,
            newsViewModel.currentFilters.toTypedArray()
        )
    }
    navController.navigate(R.id.action_newsFragment_to_filterFragment, bundle)
}

private fun openNewsDetails(
    newsItem: NewsItem,
    navController: NavController,
    newsViewModel: NewsViewModel
) {
    newsViewModel.markAsRead(newsItem.id)
    navController.navigate(
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
        newsList = persistentListOf(
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