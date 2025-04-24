package com.github.dragon925.androidlearning.help.ui.utils

import com.github.dragon925.androidlearning.common.domain.models.Category
import com.github.dragon925.androidlearning.help.ui.models.HelpCategoryItem

fun Category.toHelpCategoryItem() = HelpCategoryItem(
    id = id,
    title = name,
    icon = url
)