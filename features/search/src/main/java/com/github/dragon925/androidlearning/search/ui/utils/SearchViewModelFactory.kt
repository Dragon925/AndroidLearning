package com.github.dragon925.androidlearning.search.ui.utils

import com.github.dragon925.androidlearning.core.api.domain.models.Event
import com.github.dragon925.androidlearning.core.api.domain.models.Mapper
import com.github.dragon925.androidlearning.search.domain.usecases.SearchUseCase
import com.github.dragon925.androidlearning.search.ui.models.SearchResultItem
import com.github.dragon925.androidlearning.search.ui.viewmodels.SearchViewModel
import jakarta.inject.Inject
import jakarta.inject.Provider

internal class SearchViewModelFactory @Inject constructor(
    private val useCases: Map<Int, @JvmSuppressWildcards Provider<SearchUseCase>>,
    private val mappers: Map<Int, @JvmSuppressWildcards Provider<Mapper<Event, SearchResultItem>>>
)  {

    fun create(type: Int): SearchViewModel {
        val useCase = useCases.getValue(type).get()
        val mapper = mappers.getValue(type).get()
        return SearchViewModel(useCase, mapper)
    }
}