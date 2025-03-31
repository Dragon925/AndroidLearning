package com.github.dragon925.androidlearning.common.data.datasorces.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.github.dragon925.androidlearning.common.data.datasorces.local.entities.CategoryEntity
import com.github.dragon925.androidlearning.common.data.models.CategoryDto
import com.github.dragon925.androidlearning.common.data.toEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert
    suspend fun insertCategories(categorise: List<CategoryEntity>)

    @Query("SELECT * FROM ${CategoryEntity.TABLE_NAME}")
    fun loadCategories(): Flow<List<CategoryEntity>>

    @Transaction
    suspend fun saveCategories(categories: List<CategoryDto>) {
        clear()
        insertCategories(categories.map { it.toEntity() })
    }

    @Query("DELETE FROM ${CategoryEntity.TABLE_NAME}")
    suspend fun clear()
}