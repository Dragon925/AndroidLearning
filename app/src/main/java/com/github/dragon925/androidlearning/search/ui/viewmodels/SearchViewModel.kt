package com.github.dragon925.androidlearning.search.ui.viewmodels

import android.util.Log
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.dragon925.androidlearning.common.domain.Event
import com.github.dragon925.androidlearning.common.ui.UIState
import com.github.dragon925.androidlearning.search.data.SearchRepository
import com.github.dragon925.androidlearning.search.ui.fragments.SearchByTypeFragment
import com.github.dragon925.androidlearning.search.ui.models.SearchResultItem
import com.github.dragon925.androidlearning.search.ui.models.SearchUIState
import com.github.dragon925.androidlearning.search.ui.models.toKeywords
import com.github.dragon925.androidlearning.search.ui.models.toSearchResultItemBy
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

class SearchViewModel(
    private val loader: (keywords: List<String>) -> Observable<List<Event>>,
    private val mapper: (List<Event>) -> List<SearchResultItem>
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val searchQuery = BehaviorSubject.createDefault("")
    private val loading = BehaviorSubject.createDefault(false)
    private val data = BehaviorSubject.create<SearchUIState>()

    val viewState: Observable<UIState<SearchUIState, String>> = Observable.combineLatest(
        loading, data
    ) { loading, data ->
        UIState(
            isLoading = loading,
            data = data.takeIf { it.keywords.isNotEmpty() },
        )
    }

    init {
        searchQuery.distinctUntilChanged()
            .doOnNext { loading.onNext(true) }.switchMap { query ->
                if (query.isEmpty()) return@switchMap Observable.just(SearchUIState())

                val keywords = query.toKeywords()
                return@switchMap loader(keywords)
                    .map { events -> SearchUIState(keywords, mapper(events)) }

            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { loading.onNext(false) }
            .subscribe(
                { data.onNext(it) },
                { error -> Log.e("SearchViewModel", "Search error", error) }
            ).also { compositeDisposable.add(it) }
    }

    fun search(query: String) {
        searchQuery.onNext(query)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        const val SEARCH_TYPE = "SearchViewModel-searchType"

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val assets = this[APPLICATION_KEY]?.assets
                    ?: throw IllegalStateException("Application not found")
                val searchType = this[DEFAULT_ARGS_KEY]?.getInt(SEARCH_TYPE)
                    ?: SearchByTypeFragment.SEARCH_BY_EVENT

                SearchViewModel(
                    loader = when (searchType) {
                        SearchByTypeFragment.SEARCH_BY_NKO -> {
                            { keywords -> SearchRepository.searchOrganizers(keywords, assets) }
                        }

                        else -> {
                            { keywords -> SearchRepository.searchEvents(keywords, assets) }
                        }
                    },
                    mapper = when (searchType) {
                        SearchByTypeFragment.SEARCH_BY_NKO -> {
                            { events -> events.map { it.toSearchResultItemBy(Event::organizer) } }
                        }

                        else -> {
                            { events -> events.map { it.toSearchResultItemBy(Event::name) } }
                        }
                    }
                )
            }
        }
    }
}