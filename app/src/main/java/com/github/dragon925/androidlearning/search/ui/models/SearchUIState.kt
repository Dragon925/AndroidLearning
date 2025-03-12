package com.github.dragon925.androidlearning.search.ui.models

data class SearchUIState(
    val keywords: List<String> = emptyList(),
    val results: List<SearchResultItem> = emptyList()
)
