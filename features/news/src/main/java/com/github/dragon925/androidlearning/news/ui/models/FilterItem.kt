package com.github.dragon925.androidlearning.news.ui.models

import com.github.dragon925.androidlearning.core.api.domain.models.Category

internal data class FilterItem(
    val category: Category,
    val isChecked: Boolean = false
)