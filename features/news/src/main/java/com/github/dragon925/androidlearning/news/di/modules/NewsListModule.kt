package com.github.dragon925.androidlearning.news.di.modules

import androidx.lifecycle.ViewModel
import com.github.dragon925.androidlearning.core.api.di.ViewModelKey
import com.github.dragon925.androidlearning.news.ui.viewmodels.NewsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal fun interface NewsListModule {

    @Binds
    @[IntoMap ViewModelKey(NewsViewModel::class)]
    fun bindsNewsViewModel(viewModel: NewsViewModel): ViewModel
}