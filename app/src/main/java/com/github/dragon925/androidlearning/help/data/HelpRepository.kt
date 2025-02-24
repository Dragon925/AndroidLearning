package com.github.dragon925.androidlearning.help.data

import android.content.res.AssetManager
import android.util.Log
import com.github.dragon925.androidlearning.common.domain.Category
import com.google.gson.Gson

object HelpRepository {

    private const val CATEGORY_FILE = "categories.json"

    private val gson = Gson()

    fun getCategories(assets: AssetManager): List<Category> {
        return try {
            assets.open(CATEGORY_FILE).bufferedReader().use { inputStream ->
                gson.fromJson(inputStream, Array<Category>::class.java).toList()
            }
        } catch (e: Exception) {
            Log.e("HelpRepository-getCategories", e.message ?: "Exception")
            emptyList()
        }.sortedBy { it.name }
    }
}