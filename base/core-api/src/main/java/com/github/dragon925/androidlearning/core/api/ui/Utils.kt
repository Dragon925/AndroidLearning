package com.github.dragon925.androidlearning.core.api.ui

import android.content.Context
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
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

fun <F: Fragment, I, O> F.registerActionLauncher(
    contract: ActivityResultContract<I, O>,
    callback: ActivityResultCallback<O>
) = registerForActivityResult(contract, callback)