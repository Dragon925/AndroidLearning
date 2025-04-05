package com.github.dragon925.androidlearning.common.data.datasorces.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.dragon925.androidlearning.common.data.datasorces.local.dao.CategoryDao
import com.github.dragon925.androidlearning.common.data.datasorces.local.dao.EventDao
import com.github.dragon925.androidlearning.common.data.datasorces.local.entities.CategoryEntity
import com.github.dragon925.androidlearning.common.data.datasorces.local.entities.EventCrossCategory
import com.github.dragon925.androidlearning.common.data.datasorces.local.entities.EventEntity
import com.github.dragon925.androidlearning.common.data.datasorces.local.entities.EventPhotoEntity


@Database(
    entities = [
        CategoryEntity::class,
        EventEntity::class,
        EventPhotoEntity::class,
        EventCrossCategory::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "want-help-db"
    }

    abstract fun eventDao(): EventDao

    abstract fun categoryDao(): CategoryDao
}