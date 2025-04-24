package com.github.dragon925.androidlearning.authorization.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dragon925.androidlearning.authorization.domain.models.AuthState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _state = MutableSharedFlow<AuthState>()
    val state get() = _state.asSharedFlow()

    private val _isAuthorized = MutableStateFlow(false)
    val isAuthorized get() = _isAuthorized.asStateFlow()

    init {
        setState(AuthState.UNAUTHORIZED)
    }

    internal fun setState(state: AuthState) {
        viewModelScope.launch { _state.emit(state) }
        _isAuthorized.value = state == AuthState.AUTHORIZED
    }
}