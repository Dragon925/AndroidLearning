package com.github.dragon925.androidlearning.news.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.dragon925.androidlearning.common.data.repositories.CommonEventRepository
import com.github.dragon925.androidlearning.common.domain.Event
import com.github.dragon925.androidlearning.common.ui.UIState
import com.github.dragon925.androidlearning.news.ui.models.NewsListUIState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class NewsViewModel(
    private val loader: () -> Single<List<Event>>
) : ViewModel() {

    private val loading = BehaviorSubject.createDefault(false)
    private val readIds = BehaviorSubject.createDefault(emptySet<Int>())
    private val filters = BehaviorSubject.createDefault(emptySet<Int>())
    private val _news = BehaviorSubject.createDefault(emptyList<Event>())
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

    val currentFilters: Set<Int> get() = filters.value ?: emptySet()

    init {
        loadNews()
    }

    fun setFilters(categories: List<Int>) {
        filters.onNext(categories.toSet())
    }

    fun markAsRead(vararg ids: Int) {
        val oldIds = readIds.value ?: emptySet()
        readIds.onNext(oldIds + ids.toSet())
    }

    private fun loadNews() {
        loader().doOnSubscribe { loading.onNext(true) }
            .delay(5000, TimeUnit.MILLISECONDS) // fake loading delay
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnEvent { _, _ -> loading.onNext(false) }
            .subscribe(
                { events ->
                    _news.onNext(events)
                },
                { error ->
                    Log.e("NewsViewModel", "Error loading news", error)
                }
            ).also(compositeDisposable::add)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val assets = this[APPLICATION_KEY]?.assets
                    ?: throw IllegalStateException("Application not found")

                NewsViewModel(
                    loader = { Single.fromCallable { CommonEventRepository.getEvents(assets) } }
                )
            }
        }
    }
}