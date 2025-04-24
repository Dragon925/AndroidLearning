package com.github.dragon925.androidlearning.core.repositories

import android.util.Log
import com.github.dragon925.androidlearning.core.api.domain.models.Category
import com.github.dragon925.androidlearning.core.api.domain.repositories.CategoryRepository
import com.github.dragon925.androidlearning.core.toDomain
import com.github.dragon925.androidlearning.datasources.contract.CategoryContract
import com.github.dragon925.androidlearning.datasources.contract.LocalSource
import com.github.dragon925.androidlearning.datasources.contract.RemoteSource
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

internal class CategoryRepositoryImpl @Inject constructor(
    private val remoteSource: RemoteSource<CategoryContract>,
    private val localSource: LocalSource.Category
) : CategoryRepository {

    companion object {
        @Volatile
        private var isLoaded = false
    }

    override suspend fun preloadCategories() {
        if (isLoaded) return

        try {
            val categories = remoteSource.load()
            localSource.save(categories)
            isLoaded = true
        } catch (e: Exception) {
            Log.e("CategoryRepository", "load categories failed", e)
        }
    }

    override fun getCategories(): Flow<List<Category>> = localSource.load()
        .onStart {
            preloadCategories()
        }
        .map { categories ->
            categories.map(CategoryContract::toDomain)
                .sortedBy(Category::name)
        }
        .flowOn(Dispatchers.IO)
}