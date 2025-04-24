package com.github.dragon925.androidlearning.help.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dragon925.androidlearning.core.api.domain.models.Category
import com.github.dragon925.androidlearning.core.api.domain.repositories.CategoryRepository
import com.github.dragon925.androidlearning.core.api.ui.UIState
import com.github.dragon925.androidlearning.help.ui.models.HelpCategoryUIState
import com.github.dragon925.androidlearning.help.ui.utils.toHelpCategoryItem
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

internal class HelpCategoriesViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    private val loading = MutableStateFlow(false)
    private val categories = MutableStateFlow<HelpCategoryUIState?>(null)

    val state: Flow<UIState<HelpCategoryUIState, String>> = combine(
        loading, categories
    ) { loading, categories ->
        UIState(
            isLoading = loading,
            data = categories,
        )
    }

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            repository.getCategories()
                .onStart { loading.value = true }
                .map {
                    HelpCategoryUIState(
                        it.map(Category::toHelpCategoryItem)
                    )
                }
                .flowOn(Dispatchers.IO)
                .onEach { loading.value = false }
                .catch { error ->
                    Log.e("HelpCategoriesViewModel", "Error loading categories", error)
                }
                .collect {
                    categories.value = it
                }
        }
    }
}