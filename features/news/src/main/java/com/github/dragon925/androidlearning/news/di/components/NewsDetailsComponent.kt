package com.github.dragon925.androidlearning.news.di.components

import com.github.dragon925.androidlearning.news.di.NewsDeps
import com.github.dragon925.androidlearning.news.di.modules.NewsDetailsModule
import com.github.dragon925.androidlearning.news.di.scopes.NewsDetailsScope
import com.github.dragon925.androidlearning.news.ui.activities.NewsDetailsActivity
import dagger.BindsInstance
import dagger.Component

@Component(modules = [NewsDetailsModule::class], dependencies = [NewsDeps::class])
@NewsDetailsScope
internal fun interface NewsDetailsComponent {

    fun inject(activity: NewsDetailsActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun newsId(id: String): Builder

        fun deps(deps: NewsDeps): Builder

        fun build(): NewsDetailsComponent
    }
}