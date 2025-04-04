package com.github.dragon925.androidlearning.help.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.dragon925.androidlearning.common.data.repositories.CommonCategoryRepository
import com.github.dragon925.androidlearning.common.domain.Category
import com.github.dragon925.androidlearning.common.ui.UIState
import com.github.dragon925.androidlearning.help.ui.models.HelpCategoryItem
import com.github.dragon925.androidlearning.help.ui.models.HelpCategoryUIState
import com.github.dragon925.androidlearning.help.ui.utils.toHelpCategoryItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

class HelpCategoriesViewModel(
    private val loader: () -> Observable<List<Category>>,
    private val mapper: (List<Category>) -> List<HelpCategoryItem>
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
        loader().doOnSubscribe { loading.onNext(true) }
            .map { HelpCategoryUIState(mapper(it)) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { loading.onNext(false) }
            .subscribe(
                { categories.onNext(it) },
                { error ->
                    Log.e("NewsViewModel", "Error loading news", error)
                }
            )
            .also(compositeDisposable::add)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context = this[APPLICATION_KEY]
                    ?: throw IllegalStateException("Application not found")

                HelpCategoriesViewModel(
                    loader = {
                        CommonCategoryRepository.getCategories(context.assets)
                    },
                    mapper = { categories ->
                        categories.map { category -> category.toHelpCategoryItem() }
                    }
                )
            }
        }
    }
}