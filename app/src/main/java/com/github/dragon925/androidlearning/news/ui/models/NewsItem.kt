package com.github.dragon925.androidlearning.news.ui.models

import android.graphics.drawable.Drawable

data class NewsItem(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val categoryIds: Set<Int> = emptySet(),
    val image: Drawable? = null
)
