package com.github.dragon925.androidlearning.common.data.repositories

import android.content.res.AssetManager
import android.util.Log
import com.github.dragon925.androidlearning.common.data.EventDeserializer
import com.github.dragon925.androidlearning.common.domain.Event
import com.google.gson.GsonBuilder

object CommonEventRepository {

    private const val EVENT_FILE = "events.json"

    private val gson = GsonBuilder()
        .registerTypeAdapter(Event::class.java, EventDeserializer())
        .create()

    fun getEvents(assets: AssetManager): List<Event> = try {
        assets.open(EVENT_FILE).bufferedReader().use { inputStream ->
            gson.fromJson(inputStream, Array<Event>::class.java).toList()
        }
    } catch (e: Exception) {
        Log.e("CommonEventRepository-getEvents", e.message ?: "Exception")
        emptyList()
    }.sortedWith(
        compareBy<Event> { it.startDate }
            .thenBy { it.endDate }
            .thenBy { it.name }
    )
}