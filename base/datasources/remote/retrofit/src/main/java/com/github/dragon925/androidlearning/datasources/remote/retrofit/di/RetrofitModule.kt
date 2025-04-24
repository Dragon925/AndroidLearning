package com.github.dragon925.androidlearning.datasources.remote.retrofit.di

import com.github.dragon925.androidlearning.datasources.remote.retrofit.AppService
import com.github.dragon925.androidlearning.datasources.remote.retrofit.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
object RetrofitModule {

    @Provides
    internal fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    internal fun provideClient(loggingInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    internal fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    internal fun provideRetrofitService(
        client: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): AppService = Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .client(client)
        .addConverterFactory(converterFactory)
        .build()
        .create(AppService::class.java)
}