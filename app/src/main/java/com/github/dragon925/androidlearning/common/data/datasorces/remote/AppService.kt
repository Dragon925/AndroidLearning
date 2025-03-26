package com.github.dragon925.androidlearning.common.data.datasorces.remote

import com.github.dragon925.androidlearning.common.data.models.CategoryDto
import com.github.dragon925.androidlearning.common.data.models.EventDto
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface AppService {

    @GET("categories.json")
    fun getCategories(): Observable<List<CategoryDto>>

    @GET("events.json")
    fun getEvents(): Observable<List<EventDto>>

    @GET("events/{eventId}.json")
    fun getEvent(@Path("eventId") eventId: String): Observable<EventDto>
}