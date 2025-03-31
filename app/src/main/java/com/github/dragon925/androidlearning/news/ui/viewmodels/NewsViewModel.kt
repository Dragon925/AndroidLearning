package com.github.dragon925.androidlearning.news.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.dragon925.androidlearning.App
import com.github.dragon925.androidlearning.common.domain.Event
import com.github.dragon925.androidlearning.common.ui.UIState
import com.github.dragon925.androidlearning.news.data.repositories.NewsRepository
import com.github.dragon925.androidlearning.news.ui.models.NewsListUIState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.launch

class NewsViewModel(
    private val repository: NewsRepository
) : ViewModel() {

    private val loading = BehaviorSubject.createDefault(false)
    private val readIds = BehaviorSubject.createDefault(emptySet<String>())
    private val filters = BehaviorSubject.createDefault(emptySet<String>())
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

    val currentFilters: Set<String> get() = filters.value ?: emptySet()

    init {
        loadNews()
    }

    fun setFilters(categories: List<String>) {
        filters.onNext(categories.toSet())
    }

    fun markAsRead(vararg ids: String) {
        viewModelScope.launch {
            repository.readNews(*ids)
        }
    }

    private fun loadNews() {
        repository.getNews().doOnSubscribe { loading.onNext(true) }
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

        repository.getReadNewsIds()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(readIds::onNext)
            .also(compositeDisposable::add)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as? App
                    ?: throw IllegalStateException("Application not found")

                NewsViewModel(
                    repository = NewsRepository(database = app.database)
                )
            }
        }
    }
}