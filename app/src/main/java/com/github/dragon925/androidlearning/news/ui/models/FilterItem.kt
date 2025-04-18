package com.github.dragon925.androidlearning.news.ui.models

import android.os.Parcelable
import com.github.dragon925.androidlearning.common.domain.models.Category
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterItem(
    val category: Category,
    val isChecked: Boolean = false
) : Parcelable