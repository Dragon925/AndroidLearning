package com.github.dragon925.androidlearning.datasources.remote.retrofit

import com.github.dragon925.androidlearning.datasources.remote.retrofit.models.CategoryDto
import com.github.dragon925.androidlearning.datasources.remote.retrofit.models.EventDto
import retrofit2.http.GET

internal interface AppService {

    @GET("categories.json")
    suspend fun getCategories(): List<CategoryDto>

    @GET("events.json")
    suspend fun getEvents(): List<EventDto>
}