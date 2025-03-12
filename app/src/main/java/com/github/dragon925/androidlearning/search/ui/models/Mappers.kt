package com.github.dragon925.androidlearning.search.ui.models

import com.github.dragon925.androidlearning.common.domain.Event

fun Event.toSearchResultItemBy(text: (Event) -> String) = SearchResultItem(
    id = id,
    title = text(this),
)

fun String.toKeywords() = this.replace("[^\\w\\s-]+".toRegex(), " ")
    .lowercase()
    .split("\\s+".toRegex())
    .distinct()