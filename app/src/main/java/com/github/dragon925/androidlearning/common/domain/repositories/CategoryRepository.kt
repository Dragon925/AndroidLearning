package com.github.dragon925.androidlearning.common.domain.repositories

import com.github.dragon925.androidlearning.common.domain.models.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun preloadCategories()

    fun getCategories(): Flow<List<Category>>
}