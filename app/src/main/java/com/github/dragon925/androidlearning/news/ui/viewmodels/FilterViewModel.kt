package com.github.dragon925.androidlearning.news.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.dragon925.androidlearning.common.data.repositories.CommonCategoryRepository
import com.github.dragon925.androidlearning.common.domain.Category
import com.github.dragon925.androidlearning.common.ui.UIState
import com.github.dragon925.androidlearning.news.ui.models.FilterItem
import com.github.dragon925.androidlearning.news.ui.models.FilterUIState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.rx3.asObservable

class FilterViewModel(
    private val loader: () -> Observable<List<Category>>
) : ViewModel() {

    private val loading = BehaviorSubject.createDefault(false)
    private val categories = BehaviorSubject.createDefault(emptyList<Category>())
    private val chosenCategories = BehaviorSubject.createDefault(emptySet<String>())
    private val compositeDisposable = CompositeDisposable()

    val state: Observable<UIState<FilterUIState, String>> = Observable.combineLatest(
        loading, categories, chosenCategories
    ) { loading, categories, chosenCategories ->
        UIState(
            isLoading = loading,
            data = FilterUIState(
                categories.map { FilterItem(it, it.id in chosenCategories) }
            )
        )
    }

    val currentChosenCategories get() = chosenCategories.value ?: emptySet()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        loader().doOnSubscribe { loading.onNext(true) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { loading.onNext(false) }
            .subscribe(
                { results ->
                    categories.onNext(results)
                },
                { error ->
                    Log.e("NewsViewModel", "Error loading categories", error)
                }
            ).also(compositeDisposable::add)
    }

    fun checkCategory(vararg categoryIds: String, isChecked: Boolean) {
        val oldChosen = chosenCategories.value ?: emptySet()
        chosenCategories.onNext(
            if (isChecked) oldChosen + categoryIds.toSet() else oldChosen - categoryIds.toSet()
        )
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

                FilterViewModel(
                    loader = {
                        CommonCategoryRepository.getCategories(assets).asObservable()
                    }
                )
            }
        }
    }
}