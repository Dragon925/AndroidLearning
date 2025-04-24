package com.github.dragon925.androidlearning.news.ui.models


data class NewsListUIState(
    val newsList: List<NewsItem> = emptyList(),
    val readIds: Set<String> = emptySet(),
)