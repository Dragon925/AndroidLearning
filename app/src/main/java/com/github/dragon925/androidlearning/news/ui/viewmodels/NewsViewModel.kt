package com.github.dragon925.androidlearning.news.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dragon925.androidlearning.common.contract.Mapper
import com.github.dragon925.androidlearning.common.domain.models.Event
import com.github.dragon925.androidlearning.common.domain.repositories.EventRepository
import com.github.dragon925.androidlearning.common.ui.UIState
import com.github.dragon925.androidlearning.news.ui.models.NewsItem
import com.github.dragon925.androidlearning.news.ui.models.NewsListUIState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx3.asObservable

class NewsViewModel @Inject constructor(
    private val repository: EventRepository,
    private val mapper: Mapper<Event, NewsItem>
) : ViewModel() {

    private val loading = BehaviorSubject.createDefault(false)
    private val readIds = BehaviorSubject.createDefault(emptySet<String>())
    private val filters = BehaviorSubject.createDefault(emptySet<String>())
    private val _news = BehaviorSubject.createDefault(emptyList<NewsItem>())
    private val compositeDisposable = CompositeDisposable()

    val state: Observable<UIState<NewsListUIState, String>> = Observable.combineLatest(
        loading, readIds, filters, _news
    ) { loading, read, filters, news ->
        UIState(
            isLoading = loading,
            data = NewsListUIState(news.filter { event ->
                filters.isEmpty() || event.categoryIds.any { it in filters }
            }, read)
        )
    }

    val currentFilters: Set<String> get() = filters.value ?: emptySet()

    init {
        loadNews()
    }

    fun setFilters(categories: List<String>) {
        filters.onNext(categories.toSet())
    }

    fun markAsRead(vararg ids: String) {
        viewModelScope.launch {
            repository.readEvents(*ids)
        }
    }

    private fun loadNews() {
        repository.getEvents().asObservable().doOnSubscribe { loading.onNext(true) }
            .map {
                it.map(mapper::invoke)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterNext { loading.onNext(false) }
            .subscribe(
                { events ->
                    _news.onNext(events)
                },
                { error ->
                    Log.e("NewsViewModel", "Error loading news", error)
                }
            ).also(compositeDisposable::add)

        repository.getReadEventIds().asObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(readIds::onNext)
            .also(compositeDisposable::add)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}