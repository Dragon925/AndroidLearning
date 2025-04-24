package com.github.dragon925.androidlearning.common.data.repositories

import android.util.Log
import com.github.dragon925.androidlearning.common.contract.CategoryContract
import com.github.dragon925.androidlearning.common.data.datasorces.local.models.LocalSource
import com.github.dragon925.androidlearning.common.data.datasorces.remote.models.RemoteSource
import com.github.dragon925.androidlearning.common.data.toDomain
import com.github.dragon925.androidlearning.common.domain.models.Category
import com.github.dragon925.androidlearning.common.domain.repositories.CategoryRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class CategoryRepositoryImpl @Inject constructor(
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