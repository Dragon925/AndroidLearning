package com.github.dragon925.androidlearning.common.data.datasorces.local.models

import com.github.dragon925.androidlearning.common.contract.CategoryContract
import com.github.dragon925.androidlearning.common.data.datasorces.local.dao.CategoryDao
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow


class CategoryDataSource @Inject constructor(
    private val categoryDao: CategoryDao
) : LocalSource.Category {

    override fun load(): Flow<List<CategoryContract>> = categoryDao.loadCategories()

    override suspend fun save(data: List<CategoryContract>) = categoryDao.saveCategories(data)
}