package com.github.dragon925.androidlearning.help.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.dragon925.androidlearning.common.domain.models.Category
import com.github.dragon925.androidlearning.common.domain.repositories.CategoryRepository
import com.github.dragon925.androidlearning.common.ui.UIState
import com.github.dragon925.androidlearning.help.ui.models.HelpCategoryUIState
import com.github.dragon925.androidlearning.help.ui.utils.toHelpCategoryItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import jakarta.inject.Inject
import kotlinx.coroutines.rx3.asObservable

class HelpCategoriesViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    private val loading = BehaviorSubject.createDefault(false)
    private val categories = BehaviorSubject.create<HelpCategoryUIState>()
    private val compositeDisposable = CompositeDisposable()

    val state: Observable<UIState<HelpCategoryUIState, String>> = Observable.combineLatest(
        loading, categories
    ) { loading, categories ->
        UIState(
            isLoading = loading,
            data = categories,
        )
    }

    init {
        loadCategories()
    }

    private fun loadCategories() {
        repository.getCategories().asObservable()
            .doOnSubscribe { loading.onNext(true) }
            .map {
                HelpCategoryUIState(
                    it.map(Category::toHelpCategoryItem)
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterNext { loading.onNext(false) }
            .subscribe(
                { categories.onNext(it) },
                { error ->
                    Log.e("HelpCategoriesViewModel", "Error loading categories", error)
                }
            )
            .also(compositeDisposable::add)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}