package com.github.dragon925.androidlearning.common.data.repositories

import com.github.dragon925.androidlearning.common.domain.repositories.CategoryRepository
import jakarta.inject.Inject

class CategoryPreloader @Inject constructor(
    private val repository: CategoryRepository
) {

    suspend fun preload() {
        repository.preloadCategories()
    }
}