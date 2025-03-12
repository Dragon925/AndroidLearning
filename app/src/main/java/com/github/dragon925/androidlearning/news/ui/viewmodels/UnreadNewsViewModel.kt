package com.github.dragon925.androidlearning.news.ui.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class UnreadNewsViewModel : ViewModel() {

    private val _unreadCount = BehaviorSubject.createDefault(0)

    val unreadCount: Observable<Int> = _unreadCount.distinctUntilChanged()

    fun updateUnreadCount(unreadCount: Int) {
        _unreadCount.onNext(unreadCount)
    }
}