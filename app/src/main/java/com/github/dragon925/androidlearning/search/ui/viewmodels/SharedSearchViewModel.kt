package com.github.dragon925.androidlearning.search.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.dragon925.androidlearning.common.ui.mainComponent
import com.github.dragon925.androidlearning.search.di.SearchComponent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SharedSearchViewModel(
    val searchComponent: SearchComponent
) : ViewModel() {

    private val _query = MutableSharedFlow<String>(1)
    val query: SharedFlow<String> get() = _query.asSharedFlow()

    fun search(query: String) {
        _query.tryEmit(query)
    }

    @FlowPreview
    fun observeQuery(query: Flow<String>): Job = viewModelScope.launch {
        query.map { it.trim() }
            .debounce(500)
            .distinctUntilChanged()
            .collectLatest { _query.emit(it) }
    }

    companion object {
        val FACTORY = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] ?: throw IllegalArgumentException("No application")

                SharedSearchViewModel(app.mainComponent.searchComponent().build())
            }
        }
    }
}