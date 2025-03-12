package com.github.dragon925.androidlearning.news.ui.models

import com.github.dragon925.androidlearning.common.domain.Event

data class NewsListUIState(
    val newsList: List<Event> = emptyList(),
    val readIds: Set<Int> = emptySet(),
)