package com.github.dragon925.androidlearning.common.data.repositories

import android.content.res.AssetManager
import android.util.Log
import com.github.dragon925.androidlearning.common.domain.Category
import com.google.gson.Gson

object CommonCategoryRepository {

    private const val CATEGORY_FILE = "categories.json"

    private val gson = Gson()

    fun getCategories(assets: AssetManager): List<Category> = try {
        assets.open(CATEGORY_FILE).bufferedReader().use { inputStream ->
            gson.fromJson(inputStream, Array<Category>::class.java).toList()
        }
    } catch (e: Exception) {
        Log.e("CommonCategoryRepository-getCategories", e.message ?: "Exception")
        emptyList()
    }.sortedBy { it.name }
}