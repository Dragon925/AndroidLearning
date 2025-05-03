package com.github.dragon925.androidlearning.news.ui.models

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf


@Immutable
internal data class NewsListUIState(
    val newsList: ImmutableList<NewsItem> = persistentListOf(),
    val readIds: ImmutableSet<String> = persistentSetOf(),
)