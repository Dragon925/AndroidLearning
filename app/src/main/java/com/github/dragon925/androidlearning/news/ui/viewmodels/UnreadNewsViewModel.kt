package com.github.dragon925.androidlearning.news.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UnreadNewsViewModel : ViewModel() {

    private val _unreadCount = MutableStateFlow(0)

    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    fun updateUnreadCount(unreadCount: Int) {
        _unreadCount.value = unreadCount
    }
}