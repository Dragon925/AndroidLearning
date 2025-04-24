package com.github.dragon925.androidlearning.core.di

import com.github.dragon925.androidlearning.core.api.domain.repositories.CategoryRepository
import com.github.dragon925.androidlearning.core.api.domain.repositories.EventRepository
import com.github.dragon925.androidlearning.core.repositories.CategoryRepositoryImpl
import com.github.dragon925.androidlearning.core.repositories.EventRepositoryImpl
import com.github.dragon925.androidlearning.datasources.local.room.di.DatabaseModule
import com.github.dragon925.androidlearning.datasources.remote.retrofit.di.NetworkModule
import dagger.Binds
import dagger.Module

@Module(includes = [NetworkModule::class, DatabaseModule::class])
internal interface CoreModule {

    @Binds
    @CoreScope
    fun bindCategoryRepository(repository: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @CoreScope
    fun bindEventRepository(repository: EventRepositoryImpl): EventRepository
}