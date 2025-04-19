package com.github.dragon925.androidlearning.news.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dragon925.androidlearning.core.api.domain.models.Event
import com.github.dragon925.androidlearning.core.api.domain.models.Mapper
import com.github.dragon925.androidlearning.core.api.domain.repositories.EventRepository
import com.github.dragon925.androidlearning.core.api.ui.UIState
import com.github.dragon925.androidlearning.news.ui.models.NewsItem
import com.github.dragon925.androidlearning.news.ui.models.NewsListUIState
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


internal class NewsViewModel @Inject constructor(
    private val repository: EventRepository,
    private val mapper: Mapper<Event, NewsItem>
) : ViewModel() {

    private val loading = MutableStateFlow(false)
    private val readIds = MutableStateFlow(emptySet<String>())
    private val filters = MutableStateFlow(emptySet<String>())
    private val _news = MutableStateFlow(emptyList<NewsItem>())

    val state: Flow<UIState<NewsListUIState, String>> = combine(
        loading, readIds, filters, _news
    ) { loading, read, filters, news ->
        UIState(
            isLoading = loading,
            data = NewsListUIState(news.filter { event ->
                filters.isEmpty() || event.categoryIds.any { it in filters }
            }, read)
        )
    }

    val currentFilters: Set<String> get() = filters.value

    init {
        loadNews()
    }

    fun setFilters(categories: List<String>) {
        filters.value = categories.toSet()
    }

    fun markAsRead(vararg ids: String) {
        viewModelScope.launch {
            repository.readEvents(*ids)
        }
    }

    private fun loadNews() {
        viewModelScope.launch {
            repository.getEvents()
                .onStart { loading.value = true }
                .map { it.map(mapper::invoke) }
                .flowOn(Dispatchers.IO)
                .onEach { loading.value = false }
                .catch { error ->
                    Log.e("NewsViewModel", "Error loading news", error)
                }
                .collect {
                    _news.value = it
                }
        }

        viewModelScope.launch {
            repository.getReadEventIds()
                .flowOn(Dispatchers.IO)
                .collect {
                    readIds.value = it
                }
        }
    }
}