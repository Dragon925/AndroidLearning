package com.github.dragon925.androidlearning.help.ui.models

import android.graphics.drawable.Drawable


data class HelpCategoryItem(
    val id: Int,
    val title: String,
    val icon: Drawable? = null
)
