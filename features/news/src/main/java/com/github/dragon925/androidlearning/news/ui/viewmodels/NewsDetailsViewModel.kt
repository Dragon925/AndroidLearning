package com.github.dragon925.androidlearning.news.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dragon925.androidlearning.core.api.domain.models.Event
import com.github.dragon925.androidlearning.core.api.domain.models.Mapper
import com.github.dragon925.androidlearning.core.api.domain.repositories.EventRepository
import com.github.dragon925.androidlearning.core.api.ui.UIState
import com.github.dragon925.androidlearning.news.ui.models.NewsDetailItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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

internal class NewsDetailsViewModel @AssistedInject constructor(
    @Assisted("newsId") private val newsId: String,
    private val repository: EventRepository,
    private val mapper: Mapper<Event, NewsDetailItem>
) : ViewModel() {

    private val loading = MutableStateFlow(false)
    private val details = MutableStateFlow<NewsDetailItem?>(null)

    val state: Flow<UIState<NewsDetailItem, String>> = combine(
        loading, details
    ) { loading, event -> UIState(
        isLoading = loading,
        data = event
    ) }

    init {
        loadDetails()
    }

    private fun loadDetails() {
        viewModelScope.launch {
            repository.getEventById(newsId)
                .onStart { loading.value = true}
                .map(mapper::invoke)
                .flowOn(Dispatchers.IO)
                .onEach { loading.value = false }
                .catch { error ->
                    Log.e("NewsDetailsViewModel", "Detils load error", error)
                }
                .collect {
                    details.value = it
                }
        }
    }


    @AssistedFactory
    fun interface Factory {

        fun create(@Assisted("newsId") newsId: String): NewsDetailsViewModel
    }
}