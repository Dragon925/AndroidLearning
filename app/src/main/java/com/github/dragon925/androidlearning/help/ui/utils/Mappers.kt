package com.github.dragon925.androidlearning.help.ui.utils

import android.content.Context
import com.github.dragon925.androidlearning.common.domain.Category
import com.github.dragon925.androidlearning.common.ui.getAssetDrawable
import com.github.dragon925.androidlearning.help.ui.models.HelpCategoryItem

fun Category.toHelpCategoryItem(context: Context) = HelpCategoryItem(
    id = id,
    title = name,
    icon = context.getAssetDrawable(url)
)