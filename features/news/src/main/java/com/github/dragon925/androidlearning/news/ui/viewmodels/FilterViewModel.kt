package com.github.dragon925.androidlearning.news.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dragon925.androidlearning.core.api.domain.models.Category
import com.github.dragon925.androidlearning.core.api.domain.repositories.CategoryRepository
import com.github.dragon925.androidlearning.core.api.ui.UIState
import com.github.dragon925.androidlearning.news.ui.models.FilterItem
import com.github.dragon925.androidlearning.news.ui.models.FilterUIState
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


internal class FilterViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    private val loading = MutableStateFlow(false)
    private val categories = MutableStateFlow(emptyList<Category>())
    private val chosenCategories = MutableStateFlow(emptySet<String>())

    val state: Flow<UIState<FilterUIState, String>> = combine(
        loading, categories, chosenCategories
    ) { loading, categories, chosenCategories ->
        UIState(
            isLoading = loading,
            data = FilterUIState(
                categories.map { FilterItem(it, it.id in chosenCategories) }
            )
        )
    }

    val currentChosenCategories get() = chosenCategories.value

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            repository.getCategories()
                .onStart { loading.value = true }
                .flowOn(Dispatchers.IO)
                .onEach { loading.value = false }
                .catch { error ->
                    Log.e("NewsViewModel", "Error loading categories", error)
                }
                .collect {
                    categories.value = it
                }
        }
    }

    fun checkCategory(vararg categoryIds: String, isChecked: Boolean) {
        val oldChosen = chosenCategories.value
        chosenCategories.value = if (isChecked) {
            oldChosen + categoryIds.toSet()
        } else {
            oldChosen - categoryIds.toSet()
        }

    }
}