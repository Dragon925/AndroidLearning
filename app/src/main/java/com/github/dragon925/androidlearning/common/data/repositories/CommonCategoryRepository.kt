package com.github.dragon925.androidlearning.common.data.repositories

import android.content.res.AssetManager
import com.github.dragon925.androidlearning.common.data.datasorces.remote.AppClient
import com.github.dragon925.androidlearning.common.data.models.CategoryDto
import com.github.dragon925.androidlearning.common.data.toDomain
import com.github.dragon925.androidlearning.common.domain.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

object CommonCategoryRepository {

    private const val CATEGORY_FILE = "categories.json"

    private val helper = LoadHelper(
        fileName = CATEGORY_FILE,
        jsonClass = Array<CategoryDto>::class.java
    )

    fun getCategories(
        assets: AssetManager
    ): Flow<List<Category>> = helper.loadData(
        assets = assets,
        loader = AppClient.apiService::getCategories
    ) { categories ->
        categories.map(CategoryDto::toDomain)
            .sortedBy(Category::name)
    }
        .flowOn(Dispatchers.IO)
}