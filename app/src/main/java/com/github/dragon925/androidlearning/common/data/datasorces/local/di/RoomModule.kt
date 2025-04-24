package com.github.dragon925.androidlearning.common.data.datasorces.local.di

import android.content.Context
import androidx.room.Room
import com.github.dragon925.androidlearning.common.data.datasorces.local.AppDatabase
import dagger.Module
import dagger.Provides

@Module
object RoomModule {

    @Provides
    fun provideAppDatabase(context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        AppDatabase.DATABASE_NAME
    ).build()

    @Provides
    fun provideCategoryDao(appDatabase: AppDatabase) = appDatabase.categoryDao()

    @Provides
    fun provideEventDao(appDatabase: AppDatabase) = appDatabase.eventDao()
}