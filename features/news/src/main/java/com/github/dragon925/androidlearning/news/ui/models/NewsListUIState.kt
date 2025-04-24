package com.github.dragon925.androidlearning.news.ui.models


internal data class NewsListUIState(
    val newsList: List<NewsItem> = emptyList(),
    val readIds: Set<String> = emptySet(),
)