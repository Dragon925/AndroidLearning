package com.github.dragon925.androidlearning.datasources.local.room.di

import com.github.dragon925.androidlearning.datasources.contract.LocalSource
import com.github.dragon925.androidlearning.datasources.local.room.models.CategoryDataSource
import com.github.dragon925.androidlearning.datasources.local.room.models.EventDataSource
import dagger.Module
import dagger.Provides


@Module(includes = [RoomModule::class])
object DatabaseModule {

    @Provides
    internal fun bindCategoryDataSource(source: CategoryDataSource): LocalSource.Category = source

    @Provides
    internal fun bindEventDataSource(source: EventDataSource): LocalSource.Event = source
}