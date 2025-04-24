package com.github.dragon925.androidlearning.common.di

import com.github.dragon925.androidlearning.common.data.datasorces.local.di.DatabaseModule
import com.github.dragon925.androidlearning.common.data.datasorces.remote.di.NetworkModule
import com.github.dragon925.androidlearning.common.data.repositories.CategoryRepositoryImpl
import com.github.dragon925.androidlearning.common.data.repositories.EventRepositoryImpl
import com.github.dragon925.androidlearning.common.domain.repositories.CategoryRepository
import com.github.dragon925.androidlearning.common.domain.repositories.EventRepository
import com.github.dragon925.androidlearning.help.di.HelpComponent
import com.github.dragon925.androidlearning.news.di.components.NewsDetailsComponent
import com.github.dragon925.androidlearning.news.di.components.NewsListComponent
import com.github.dragon925.androidlearning.profile.di.ProfileComponent
import com.github.dragon925.androidlearning.search.di.SearchComponent
import dagger.Binds
import dagger.Module
import jakarta.inject.Singleton

@Module(
    includes = [
        DatabaseModule::class, NetworkModule::class,
    ],
    subcomponents = [
        SearchComponent::class, ProfileComponent::class, HelpComponent::class,
        NewsListComponent::class, NewsDetailsComponent::class
    ]
)
interface AppModule {

    @Binds
    @Singleton
    fun bindCategoryRepository(repository: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @Singleton
    fun bindEventRepository(repository: EventRepositoryImpl): EventRepository
}