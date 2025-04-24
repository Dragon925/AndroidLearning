package com.github.dragon925.androidlearning.common.di

import android.content.Context
import com.github.dragon925.androidlearning.help.di.HelpComponent
import com.github.dragon925.androidlearning.news.di.components.NewsDetailsComponent
import com.github.dragon925.androidlearning.news.di.components.NewsListComponent
import com.github.dragon925.androidlearning.profile.di.ProfileComponent
import com.github.dragon925.androidlearning.search.di.SearchComponent
import dagger.BindsInstance
import dagger.Component
import jakarta.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

    fun searchComponent(): SearchComponent.Builder
    fun profileComponent(): ProfileComponent.Builder
    fun helpComponent(): HelpComponent.Builder
    fun newsListComponent(): NewsListComponent.Builder
    fun newsDetailsComponent(): NewsDetailsComponent.Builder

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}