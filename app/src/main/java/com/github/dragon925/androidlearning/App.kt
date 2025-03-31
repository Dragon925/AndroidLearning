package com.github.dragon925.androidlearning

import android.app.Application
import androidx.room.Room
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.CachePolicy
import coil3.request.crossfade
import com.github.dragon925.androidlearning.common.data.datasorces.local.AppDatabase

class App : Application(), SingletonImageLoader.Factory {

    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, AppDatabase.DATABASE_NAME
        ).build()
    }

    override fun newImageLoader(context: PlatformContext) = ImageLoader.Builder(context)
        .crossfade(true)
        .diskCachePolicy(CachePolicy.DISABLED)
        .memoryCachePolicy(CachePolicy.DISABLED)
        .build()
}