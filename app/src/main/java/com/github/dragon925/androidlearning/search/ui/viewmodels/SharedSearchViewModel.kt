package com.github.dragon925.androidlearning.search.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SharedSearchViewModel : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> get() = _query.asStateFlow()

    fun search(query: String) {
        _query.value = query
    }

    @FlowPreview
    fun observeQuery(query: Flow<String>): Job = viewModelScope.launch {
            query.map { it.trim() }
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { _query.value = it }
        }
}