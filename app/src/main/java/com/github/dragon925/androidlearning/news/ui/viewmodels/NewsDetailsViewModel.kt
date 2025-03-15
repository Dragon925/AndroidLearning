package com.github.dragon925.androidlearning.news.ui.viewmodels

import android.util.Log
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.dragon925.androidlearning.common.data.repositories.CommonEventRepository
import com.github.dragon925.androidlearning.common.domain.Event
import com.github.dragon925.androidlearning.common.ui.UIState
import com.github.dragon925.androidlearning.news.ui.models.NewsDetailItem
import com.github.dragon925.androidlearning.news.ui.utils.toNewsDetailItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class NewsDetailsViewModel(
    private val loader: () -> Single<Event>,
    private val mapper: (Event) -> NewsDetailItem
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
        loader().doOnSubscribe { loading.onNext(true) }
            .map(mapper)
            .delay(5000, TimeUnit.MILLISECONDS) // fake loading delay
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnEvent { _, _ -> loading.onNext(false) }
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

    companion object {
        const val NEWS_DETAILS_ID = "NewsDetailsViewModel-id"

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context = this[APPLICATION_KEY]
                    ?: throw IllegalStateException("Application not found")

                val id = this[DEFAULT_ARGS_KEY]?.getInt(NEWS_DETAILS_ID)
                    ?: throw IllegalStateException("News Details Id not found")

                NewsDetailsViewModel(
                    loader = {
                        Single.fromCallable { CommonEventRepository.getEvents(context.assets) }
                            .map { events -> events.first { it.id == id } }
                    },
                    mapper = { it.toNewsDetailItem(context) }
                )
            }
        }
    }
}