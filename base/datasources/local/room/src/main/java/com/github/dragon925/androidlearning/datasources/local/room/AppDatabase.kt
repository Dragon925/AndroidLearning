package com.github.dragon925.androidlearning.datasources.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.dragon925.androidlearning.datasources.local.room.dao.CategoryDao
import com.github.dragon925.androidlearning.datasources.local.room.dao.EventDao
import com.github.dragon925.androidlearning.datasources.local.room.entities.CategoryEntity
import com.github.dragon925.androidlearning.datasources.local.room.entities.EventCrossCategory
import com.github.dragon925.androidlearning.datasources.local.room.entities.EventEntity
import com.github.dragon925.androidlearning.datasources.local.room.entities.EventPhotoEntity


@Database(
    entities = [
        CategoryEntity::class,
        EventEntity::class,
        EventPhotoEntity::class,
        EventCrossCategory::class
    ],
    version = 1
)
internal abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "want-help-db"
    }

    abstract fun eventDao(): EventDao

    abstract fun categoryDao(): CategoryDao
}