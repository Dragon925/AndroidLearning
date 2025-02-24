package com.github.dragon925.androidlearning.news.ui.models

import com.github.dragon925.androidlearning.common.domain.Category

data class FilterItem(
    val category: Category,
    val isChecked: Boolean = false
)