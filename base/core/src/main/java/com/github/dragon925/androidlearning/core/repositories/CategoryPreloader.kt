package com.github.dragon925.androidlearning.core.repositories

import com.github.dragon925.androidlearning.core.api.domain.repositories.CategoryRepository
import jakarta.inject.Inject

internal class CategoryPreloader @Inject constructor(
    private val repository: CategoryRepository
) {

    suspend fun preload() {
        repository.preloadCategories()
    }
}