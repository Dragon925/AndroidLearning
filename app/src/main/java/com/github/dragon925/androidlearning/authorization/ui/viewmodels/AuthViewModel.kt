package com.github.dragon925.androidlearning.authorization.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _isSuccess = MutableSharedFlow<Boolean>()
    val success get() = _isSuccess.asSharedFlow()

    fun setSuccess(success: Boolean) {
        viewModelScope.launch { _isSuccess.emit(success) }
    }
}