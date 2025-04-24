package com.github.dragon925.androidlearning.common.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.dragon925.androidlearning.common.di.AppComponent

class ComponentViewModel<T>(
    val component: T
) : ViewModel() {

    companion object {
        inline fun <reified T> createBy(
            crossinline builder: AppComponent.() -> T
        ) = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] ?: throw IllegalArgumentException("Missing application")

                ComponentViewModel(app.mainComponent.builder())
            }
        }

        inline fun <reified T, reified P> createBy(
            component: P,
            crossinline builder: P.() -> T
        ) = viewModelFactory {
            initializer {
                ComponentViewModel(component.builder())
            }
        }
    }
}