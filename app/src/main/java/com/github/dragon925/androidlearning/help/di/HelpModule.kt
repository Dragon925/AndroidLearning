package com.github.dragon925.androidlearning.help.di

import androidx.lifecycle.ViewModel
import com.github.dragon925.androidlearning.common.di.ViewModelKey
import com.github.dragon925.androidlearning.help.ui.viewmodels.HelpCategoriesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
fun interface HelpModule {

    @Binds
    @[IntoMap ViewModelKey(HelpCategoriesViewModel::class)]
    fun bindHelpCategoriesViewModel(viewModel: HelpCategoriesViewModel): ViewModel
}