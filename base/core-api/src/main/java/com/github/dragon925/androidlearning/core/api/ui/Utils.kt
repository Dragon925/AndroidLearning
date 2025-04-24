package com.github.dragon925.androidlearning.core.api.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.dragon925.androidlearning.core.api.domain.models.FeatureDeps

fun Context.dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()

inline fun <reified D : FeatureDeps> Context.getDeps() = when (this) {
    is D -> this
    else -> (this.applicationContext as? D)
        ?: throw IllegalStateException("Context is not an instance of ${D::class.qualifiedName}")
}

inline fun <reified VM: ViewModel> createFactoryByViewModel(
    crossinline creator: () -> VM
): ViewModelProvider.Factory = viewModelFactory {
    initializer<VM> {
        creator()
    }
}

inline fun <reified VM: ViewModel> MultiViewModelFactory.create(): VM = this.create(VM::class.java)