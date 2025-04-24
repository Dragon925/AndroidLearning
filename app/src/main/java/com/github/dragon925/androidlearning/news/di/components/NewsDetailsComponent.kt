package com.github.dragon925.androidlearning.news.di.components

import com.github.dragon925.androidlearning.news.di.modules.NewsDetailsModule
import com.github.dragon925.androidlearning.news.di.scopes.NewsDetailsScope
import com.github.dragon925.androidlearning.news.ui.activities.NewsDetailsActivity
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [NewsDetailsModule::class])
@NewsDetailsScope
fun interface NewsDetailsComponent {

    fun inject(activity: NewsDetailsActivity)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun newsId(id: String): Builder

        fun build(): NewsDetailsComponent
    }
}