package com.github.dragon925.androidlearning.search.ui.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class SharedSearchViewModel : ViewModel() {

    private val _query = BehaviorSubject.createDefault("")
    val query: Observable<String> get() = _query.distinctUntilChanged()

    fun search(query: String) {
        _query.onNext(query)
    }

    fun observeQuery(query: Observable<CharSequence>): Disposable =
        query.map { it.toString().trim() }
            .debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::search)
}