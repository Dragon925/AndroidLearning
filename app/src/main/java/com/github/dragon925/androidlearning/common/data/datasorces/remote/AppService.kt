package com.github.dragon925.androidlearning.common.data.datasorces.remote

import com.github.dragon925.androidlearning.common.data.models.CategoryDto
import com.github.dragon925.androidlearning.common.data.models.EventDto
import retrofit2.http.GET
import retrofit2.http.Path

interface AppService {

    @GET("categories.json")
    suspend fun getCategories(): List<CategoryDto>

    @GET("events.json")
    suspend fun getEvents(): List<EventDto>

    @GET("events/{eventId}.json")
    suspend fun getEvent(@Path("eventId") eventId: String): EventDto
}