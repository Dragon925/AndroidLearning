package com.github.dragon925.androidlearning.common.data.repositories

import android.content.res.AssetManager
import android.util.Log
import com.github.dragon925.androidlearning.common.data.datasorces.remote.AppClient
import com.github.dragon925.androidlearning.common.data.models.CategoryDto
import com.github.dragon925.androidlearning.common.data.toDomain
import com.github.dragon925.androidlearning.common.domain.Category
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

object CommonCategoryRepository {

    private const val CATEGORY_FILE = "categories.json"

    private val gson = Gson()

    fun getCategories(
        assets: AssetManager
    ): Observable<List<Category>> = AppClient.apiService.getCategories()
        .onErrorResumeNext { getCategoriesFromFile(assets) }
        .map { categories ->
            categories.map(CategoryDto::toDomain)
                .sortedBy { it.name }
        }
        .subscribeOn(Schedulers.io())

    private fun getCategoriesFromFile(
        assets: AssetManager
    ): Observable<List<CategoryDto>> = Observable.fromCallable {
        return@fromCallable try {
            assets.open(CATEGORY_FILE).bufferedReader().use { inputStream ->
                gson.fromJson(inputStream, Array<CategoryDto>::class.java).toList()
            }
        } catch (e: Exception) {
            Log.e("CommonCategoryRepository-getCategories", "get categories failed", e)
            emptyList()
        }
    }
}