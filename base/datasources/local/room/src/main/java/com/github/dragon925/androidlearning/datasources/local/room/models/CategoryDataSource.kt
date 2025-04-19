package com.github.dragon925.androidlearning.datasources.local.room.models

import com.github.dragon925.androidlearning.datasources.contract.CategoryContract
import com.github.dragon925.androidlearning.datasources.contract.LocalSource
import com.github.dragon925.androidlearning.datasources.local.room.dao.CategoryDao
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow


internal class CategoryDataSource @Inject constructor(
    private val categoryDao: CategoryDao
) : LocalSource.Category {

    override fun load(): Flow<List<CategoryContract>> = categoryDao.loadCategories()

    override suspend fun save(data: List<CategoryContract>) = categoryDao.saveCategories(data)
}