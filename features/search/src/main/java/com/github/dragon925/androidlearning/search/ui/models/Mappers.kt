package com.github.dragon925.androidlearning.search.ui.models

import com.github.dragon925.androidlearning.core.api.domain.models.Event


internal fun Event.toSearchResultItemBy(text: (Event) -> String) = SearchResultItem(
    id = id,
    title = text(this),
)

internal fun String.toKeywords() = this.replace("[^\\w\\s-]+".toRegex(), " ")
    .lowercase()
    .split("\\s+".toRegex())
    .distinct()