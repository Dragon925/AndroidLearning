package com.github.dragon925.androidlearning.news.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.dragon925.androidlearning.common.contract.Mapper
import com.github.dragon925.androidlearning.common.domain.models.Event
import com.github.dragon925.androidlearning.common.domain.repositories.EventRepository
import com.github.dragon925.androidlearning.common.ui.UIState
import com.github.dragon925.androidlearning.news.ui.models.NewsDetailItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.rx3.asObservable

class NewsDetailsViewModel @AssistedInject constructor(
    @Assisted("newsId") private val newsId: String,
    private val repository: EventRepository,
    private val mapper: Mapper<Event, NewsDetailItem>
) : ViewModel() {

    private val loading = BehaviorSubject.createDefault(false)
    private val details = BehaviorSubject.create<NewsDetailItem>()
    private val compositeDisposable = CompositeDisposable()

    val state: Observable<UIState<NewsDetailItem, String>> = Observable.combineLatest(
        loading, details
    ) { loading, event -> UIState(
        isLoading = loading,
        data = event
    ) }

    init {
        loadDetails()
    }

    private fun loadDetails() {
        repository.getEventById(newsId).asObservable().doOnSubscribe { loading.onNext(true) }
            .map(mapper::invoke)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterNext { loading.onNext(false) }
            .subscribe(
                { details.onNext(it) },
                { error ->
                    Log.e("NewsDetailsViewModel", "Detils load error", error)
                }
            ).also(compositeDisposable::add)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    @AssistedFactory
    fun interface Factory {

        fun create(@Assisted("newsId") newsId: String): NewsDetailsViewModel
    }
}