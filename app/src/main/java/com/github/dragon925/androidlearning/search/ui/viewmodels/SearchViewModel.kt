package com.github.dragon925.androidlearning.search.ui.viewmodels

import android.util.Log
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.dragon925.androidlearning.common.domain.Event
import com.github.dragon925.androidlearning.common.ui.UIState
import com.github.dragon925.androidlearning.search.data.SearchRepository
import com.github.dragon925.androidlearning.search.ui.fragments.SearchByTypeFragment
import com.github.dragon925.androidlearning.search.ui.models.SearchResultItem
import com.github.dragon925.androidlearning.search.ui.models.SearchUIState
import com.github.dragon925.androidlearning.search.ui.models.toKeywords
import com.github.dragon925.androidlearning.search.ui.models.toSearchResultItemBy
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
class SearchViewModel(
    private val loader: (keywords: List<String>) -> Flow<List<Event>>,
    private val mapper: (List<Event>) -> List<SearchResultItem>
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
                    return@flatMapLatest loader(keywords)
                        .map { events ->
                            SearchUIState(keywords, mapper(events))
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

    companion object {
        const val SEARCH_TYPE = "SearchViewModel-searchType"

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val assets = this[APPLICATION_KEY]?.assets
                    ?: throw IllegalStateException("Application not found")
                val searchType = this[DEFAULT_ARGS_KEY]?.getInt(SEARCH_TYPE)
                    ?: SearchByTypeFragment.SEARCH_BY_EVENT

                SearchViewModel(
                    loader = when (searchType) {
                        SearchByTypeFragment.SEARCH_BY_NKO -> {
                            { keywords -> SearchRepository.searchOrganizers(keywords, assets) }
                        }

                        else -> {
                            { keywords -> SearchRepository.searchEvents(keywords, assets) }
                        }
                    },
                    mapper = when (searchType) {
                        SearchByTypeFragment.SEARCH_BY_NKO -> {
                            { events -> events.map { it.toSearchResultItemBy(Event::organizer) } }
                        }

                        else -> {
                            { events -> events.map { it.toSearchResultItemBy(Event::name) } }
                        }
                    }
                )
            }
        }
    }
}