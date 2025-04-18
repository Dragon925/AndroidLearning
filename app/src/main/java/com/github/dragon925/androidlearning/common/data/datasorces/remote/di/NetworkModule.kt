package com.github.dragon925.androidlearning.common.data.datasorces.remote.di

import com.github.dragon925.androidlearning.common.contract.CategoryContract
import com.github.dragon925.androidlearning.common.contract.EventContract
import com.github.dragon925.androidlearning.common.data.datasorces.remote.AppService
import com.github.dragon925.androidlearning.common.data.datasorces.remote.models.RemoteSource
import dagger.Module
import dagger.Provides

@Module(includes = [RetrofitModule::class])
object NetworkModule {

    @Provides
    fun provideEventRemoteSource(
        appService: AppService
    ): RemoteSource<EventContract> = RemoteSource { appService.getEvents() }

    @Provides
    fun provideCategoryRemoteSource(
        appService: AppService
    ): RemoteSource<CategoryContract> = RemoteSource { appService.getCategories() }
}