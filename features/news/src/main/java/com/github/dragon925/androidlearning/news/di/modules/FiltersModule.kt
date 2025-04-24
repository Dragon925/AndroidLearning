package com.github.dragon925.androidlearning.news.di.modules

import androidx.lifecycle.ViewModel
import com.github.dragon925.androidlearning.core.api.di.ViewModelKey
import com.github.dragon925.androidlearning.news.ui.viewmodels.FilterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal fun interface FiltersModule {

    @Binds
    @[IntoMap ViewModelKey(FilterViewModel::class)]
    fun bindsFilterViewModel(viewModel: FilterViewModel): ViewModel
}