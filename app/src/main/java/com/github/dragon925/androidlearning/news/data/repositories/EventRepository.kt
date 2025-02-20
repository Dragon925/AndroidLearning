package com.github.dragon925.androidlearning.news.data.repositories

import android.content.res.AssetManager
import com.github.dragon925.androidlearning.common.domain.Category
import com.github.dragon925.androidlearning.news.data.models.Event
import com.github.dragon925.androidlearning.news.data.utils.EventDeserializer
import com.google.gson.GsonBuilder

object EventRepository {

    private const val EVENT_FILE = "events.json"
    private const val CATEGORY_FILE = "categories.json"

    private val gson = GsonBuilder()
        .registerTypeAdapter(Event::class.java, EventDeserializer())
        .create()

    fun getEvents(assets: AssetManager): List<Event> {
        val events = mutableListOf<Event>()
        try {
            assets.open(EVENT_FILE).bufferedReader().use { inputStream ->
                gson.fromJson(inputStream, Array<Event>::class.java)
                    .forEach { events.add(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return events.sortedWith(
            compareBy<Event> { it.startDate }
                .thenBy { it.endDate }
                .thenBy { it.name }
        )
    }

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