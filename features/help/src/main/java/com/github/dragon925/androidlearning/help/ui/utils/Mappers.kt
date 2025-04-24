package com.github.dragon925.androidlearning.help.ui.utils

import com.github.dragon925.androidlearning.core.api.domain.models.Category
import com.github.dragon925.androidlearning.help.ui.models.HelpCategoryItem

internal fun Category.toHelpCategoryItem() = HelpCategoryItem(
    id = id,
    title = name,
    icon = url
)