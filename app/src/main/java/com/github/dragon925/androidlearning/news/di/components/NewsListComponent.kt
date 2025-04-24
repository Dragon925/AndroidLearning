package com.github.dragon925.androidlearning.news.di.components

import com.github.dragon925.androidlearning.news.di.modules.NewsModule
import com.github.dragon925.androidlearning.news.di.scopes.NewsListScope
import com.github.dragon925.androidlearning.news.ui.fragments.NewsFragment
import dagger.Subcomponent

@Subcomponent(modules = [NewsModule::class])
@NewsListScope
interface NewsListComponent {

    fun filterComponent(): FilterComponent.Builder

    fun inject(newsFragment: NewsFragment)

    @Subcomponent.Builder
    fun interface Builder {

        fun build(): NewsListComponent
    }
}