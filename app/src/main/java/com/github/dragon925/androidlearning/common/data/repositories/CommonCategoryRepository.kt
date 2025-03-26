package com.github.dragon925.androidlearning.common.data.repositories

import android.content.res.AssetManager
import android.util.Log
import com.github.dragon925.androidlearning.common.data.datasorces.remote.AppClient
import com.github.dragon925.androidlearning.common.data.models.CategoryDto
import com.github.dragon925.androidlearning.common.data.toDomain
import com.github.dragon925.androidlearning.common.domain.Category
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

object CommonCategoryRepository {

    private const val CATEGORY_FILE = "categories.json"

    private val gson = Gson()

    fun getCategories(
        assets: AssetManager
    ): Flow<List<Category>> = flow {
        emit(AppClient.apiService.getCategories())
    }
        .catch { emit(getCategoriesFromFile(assets)) }
        .map { categories ->
            categories.map(CategoryDto::toDomain)
                .sortedBy { it.name }
        }
        .flowOn(Dispatchers.IO)

    private suspend fun getCategoriesFromFile(
        assets: AssetManager,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): List<CategoryDto> = withContext(dispatcher) {
        return@withContext try {
            assets.open(CATEGORY_FILE).bufferedReader().use { inputStream ->
                gson.fromJson(inputStream, Array<CategoryDto>::class.java).toList()
            }
        } catch (e: Exception) {
            Log.e("CommonCategoryRepository-getCategories", "get categories failed", e)
            emptyList()
        }
    }
}