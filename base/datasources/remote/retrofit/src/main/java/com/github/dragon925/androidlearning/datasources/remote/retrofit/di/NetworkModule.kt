package com.github.dragon925.androidlearning.datasources.remote.retrofit.di

import com.github.dragon925.androidlearning.datasources.contract.CategoryContract
import com.github.dragon925.androidlearning.datasources.contract.EventContract
import com.github.dragon925.androidlearning.datasources.contract.RemoteSource
import com.github.dragon925.androidlearning.datasources.remote.retrofit.AppService
import dagger.Module
import dagger.Provides


@Module(includes = [RetrofitModule::class])
object NetworkModule {

    @Provides
    internal fun provideEventRemoteSource(
        appService: AppService
    ): RemoteSource<EventContract> = RemoteSource { appService.getEvents() }


    @Provides
    internal fun provideCategoryRemoteSource(
        appService: AppService
    ): RemoteSource<CategoryContract> = RemoteSource { appService.getCategories() }
}