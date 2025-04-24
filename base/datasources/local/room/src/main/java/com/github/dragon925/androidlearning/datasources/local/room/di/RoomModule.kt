package com.github.dragon925.androidlearning.datasources.local.room.di

import android.content.Context
import androidx.room.Room
import com.github.dragon925.androidlearning.datasources.local.room.AppDatabase
import dagger.Module
import dagger.Provides

@Module
object RoomModule {

    @Provides
    internal fun provideAppDatabase(context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        AppDatabase.DATABASE_NAME
    ).build()

    @Provides
    internal fun provideCategoryDao(appDatabase: AppDatabase) = appDatabase.categoryDao()

    @Provides
    internal fun provideEventDao(appDatabase: AppDatabase) = appDatabase.eventDao()
}