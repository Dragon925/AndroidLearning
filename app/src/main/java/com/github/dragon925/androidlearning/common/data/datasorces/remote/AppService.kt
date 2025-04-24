package com.github.dragon925.androidlearning.common.data.datasorces.remote

import com.github.dragon925.androidlearning.common.data.datasorces.remote.models.CategoryDto
import com.github.dragon925.androidlearning.common.data.datasorces.remote.models.EventDto
import retrofit2.http.GET

interface AppService {

    @GET("categories.json")
    suspend fun getCategories(): List<CategoryDto>

    @GET("events.json")
    suspend fun getEvents(): List<EventDto>
}