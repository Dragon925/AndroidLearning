package com.github.dragon925.androidlearning.help.data

import android.content.res.AssetManager
import com.github.dragon925.androidlearning.common.domain.Category
import com.google.gson.Gson

object HelpRepository {

    private const val CATEGORY_FILE = "categories.json"

    private val gson = Gson()

    fun getCategories(assets: AssetManager): List<Category> {
        val categories = mutableListOf<Category>()
        try {
            assets.open(CATEGORY_FILE).bufferedReader().use { inputStream ->
                gson.fromJson(inputStream, Array<Category>::class.java)
                    .forEach { categories.add(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return categories.sortedBy { it.name }
    }
}