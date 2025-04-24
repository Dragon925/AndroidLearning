package com.github.dragon925.androidlearning.search.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dragon925.androidlearning.core.api.domain.models.Event
import com.github.dragon925.androidlearning.core.api.domain.models.Mapper
import com.github.dragon925.androidlearning.core.api.ui.UIState
import com.github.dragon925.androidlearning.search.domain.usecases.SearchUseCase
import com.github.dragon925.androidlearning.search.ui.models.SearchResultItem
import com.github.dragon925.androidlearning.search.ui.models.SearchUIState
import com.github.dragon925.androidlearning.search.ui.models.toKeywords
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Suppress("OPT_IN_USAGE")
internal class SearchViewModel(
    private val searchUseCase: SearchUseCase,
    private val mapper: Mapper<Event, SearchResultItem>
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val loading = MutableStateFlow(false)
    private val data = MutableStateFlow(SearchUIState())

    val viewState: Flow<UIState<SearchUIState, String>> = loading.combine(data) { loading, data ->
        UIState(
            isLoading = loading,
            data = data.takeIf { it.keywords.isNotEmpty() },
        )
    }

    init {
        viewModelScope.launch {
            searchQuery.onEach { loading.value = true }
                .flatMapLatest { query ->
                    if (query.isEmpty()) return@flatMapLatest flowOf(SearchUIState())

                    val keywords = query.toKeywords()
                    return@flatMapLatest searchUseCase.search(keywords)
                        .map { events ->
                            SearchUIState(keywords, events.map(mapper::invoke))
                        }
                }.flowOn(Dispatchers.IO)
                .onEach { loading.value = false }
                .catch { error ->
                    Log.e("SearchViewModel", "Search error", error)
                }
                .collect { data.value = it }
        }
    }

    fun search(query: String) {
        searchQuery.value = query
    }
}