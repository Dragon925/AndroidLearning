package com.github.dragon925.androidlearning.common.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.dragon925.androidlearning.App
import com.github.dragon925.androidlearning.common.di.AppComponent

fun Context.dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()

val Context.mainComponent: AppComponent
    get() = when(this) {
    is App -> appComponent
    else -> (this.applicationContext as App).appComponent
}

inline fun <reified VM: ViewModel> createFactoryByViewModel(
    crossinline creator: () -> VM
): ViewModelProvider.Factory = viewModelFactory {
    initializer<VM> {
        creator()
    }
}

inline fun <reified VM: ViewModel> MultiViewModelFactory.create(): VM = this.create(VM::class.java)