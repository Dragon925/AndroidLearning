package com.github.dragon925.androidlearning.datasources.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.github.dragon925.androidlearning.datasources.contract.CategoryContract
import com.github.dragon925.androidlearning.datasources.local.room.entities.CategoryEntity
import com.github.dragon925.androidlearning.datasources.local.room.toEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CategoryDao {

    @Insert
    suspend fun insertCategories(categorise: List<CategoryEntity>)

    @Query("SELECT * FROM ${CategoryEntity.TABLE_NAME}")
    fun loadCategories(): Flow<List<CategoryEntity>>

    @Transaction
    suspend fun saveCategories(categories: List<CategoryContract>) {
        clear()
        insertCategories(categories.map(CategoryContract::toEntity))
    }

    @Query("DELETE FROM ${CategoryEntity.TABLE_NAME}")
    suspend fun clear()
}