package com.github.dragon925.androidlearning.common.data.datasorces.local.di

import com.github.dragon925.androidlearning.common.data.datasorces.local.models.CategoryDataSource
import com.github.dragon925.androidlearning.common.data.datasorces.local.models.EventDataSource
import com.github.dragon925.androidlearning.common.data.datasorces.local.models.LocalSource
import dagger.Binds
import dagger.Module


@Module(includes = [RoomModule::class])
interface DatabaseModule {

    @Binds
    fun bindCategoryDataSource(source: CategoryDataSource): LocalSource.Category

    @Binds
    fun bindEventDataSource(source: EventDataSource): LocalSource.Event
}