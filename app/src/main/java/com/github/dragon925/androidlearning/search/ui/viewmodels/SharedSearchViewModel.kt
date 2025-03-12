package com.github.dragon925.androidlearning.search.ui.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class SharedSearchViewModel : ViewModel() {

    private val _query = BehaviorSubject.createDefault("")
    val query: Observable<String> get() = _query.distinctUntilChanged()

    fun search(query: String) {
        _query.onNext(query)
    }
}