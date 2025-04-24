package com.github.dragon925.androidlearning.core.api.domain.repositories

import com.github.dragon925.androidlearning.core.api.domain.models.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun preloadCategories()

    fun getCategories(): Flow<List<Category>>
}