package com.github.dragon925.androidlearning.common.data.repositories

import android.util.Log
import com.github.dragon925.androidlearning.common.data.datasorces.local.AppDatabase
import com.github.dragon925.androidlearning.common.data.datasorces.local.entities.CategoryEntity
import com.github.dragon925.androidlearning.common.data.datasorces.remote.AppClient
import com.github.dragon925.androidlearning.common.data.toDomain
import com.github.dragon925.androidlearning.common.domain.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

object CommonCategoryRepository {

    @Volatile
    private var isLoaded = false

    suspend fun preloadCategories(db: AppDatabase) {
        if (isLoaded) return

        try {
            val categories = AppClient.apiService.getCategories()
            db.categoryDao().saveCategories(categories)
            isLoaded = true
        } catch (e: Exception) {
            Log.e("CategoryRepository", "load categories failed", e)
        }
    }

    fun getCategories(db: AppDatabase): Flow<List<Category>> = db.categoryDao().loadCategories()
        .onStart {
            preloadCategories(db)
        }
        .map { categories ->
            categories.map(CategoryEntity::toDomain)
                .sortedBy { it.name }
        }
        .flowOn(Dispatchers.IO)
}