package com.github.dragon925.androidlearning.common.ui

data class UIState<S, E>(
    val isLoading: Boolean = false,
    val error: E? = null,
    val data: S? = null
) {
    val isError = error != null
    val isCorrect = isLoading || isError || data != null
}