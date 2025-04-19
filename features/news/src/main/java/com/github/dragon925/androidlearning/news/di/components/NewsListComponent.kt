package com.github.dragon925.androidlearning.news.di.components

import com.github.dragon925.androidlearning.news.di.NewsDeps
import com.github.dragon925.androidlearning.news.di.modules.NewsModule
import com.github.dragon925.androidlearning.news.di.scopes.NewsListScope
import com.github.dragon925.androidlearning.news.ui.fragments.NewsFragment
import dagger.Component

@Component(modules = [NewsModule::class], dependencies = [NewsDeps::class])
@NewsListScope
internal interface NewsListComponent {

    fun filterComponent(): FilterComponent.Builder

    fun inject(newsFragment: NewsFragment)

    @Component.Builder
    interface Builder {

        fun deps(deps: NewsDeps): Builder

        fun build(): NewsListComponent
    }
}