package com.github.dragon925.androidlearning.news.ui.models

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf

@Immutable
internal data class NewsItem(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val categoryIds: ImmutableSet<String> = persistentSetOf(),
    val image: String? = null
)
