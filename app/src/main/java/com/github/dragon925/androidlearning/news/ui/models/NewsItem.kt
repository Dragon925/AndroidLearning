package com.github.dragon925.androidlearning.news.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsItem(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val categoryIds: Set<String> = emptySet(),
    val image: String? = null
) : Parcelable
