package com.github.dragon925.androidlearning.core.api.ui

import androidx.lifecycle.ViewModel
import jakarta.inject.Inject
import jakarta.inject.Provider

class MultiViewModelFactory @Inject constructor(
    private val viewModelFactories: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) {

    @Suppress("UNCHECKED_CAST")
    fun <T: ViewModel> create(modelClass: Class<T>): T {
        return viewModelFactories.getValue(modelClass as Class<out ViewModel>).get() as T
    }
}