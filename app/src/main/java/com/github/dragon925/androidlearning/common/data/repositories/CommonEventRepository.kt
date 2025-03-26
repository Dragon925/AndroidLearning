package com.github.dragon925.androidlearning.common.data.repositories

import android.content.res.AssetManager
import android.util.Log
import com.github.dragon925.androidlearning.common.data.datasorces.remote.AppClient
import com.github.dragon925.androidlearning.common.data.models.EventDto
import com.github.dragon925.androidlearning.common.data.toDomain
import com.github.dragon925.androidlearning.common.domain.Event
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

object CommonEventRepository {

    private const val EVENT_FILE = "events.json"

    private val gson = Gson()

    fun getEvents(
        assets: AssetManager
    ): Observable<List<Event>> = AppClient.apiService.getEvents()
        .onErrorResumeNext { getEventsFromAssets(assets) }
        .map { events -> events.map(EventDto::toDomain)
            .sortedWith(
                compareBy<Event> { it.startDate }
                    .thenBy { it.endDate }
                    .thenBy { it.name }
            )
        }
        .subscribeOn(Schedulers.io())

    fun getEventById(
        eventId: String,
        assets: AssetManager
    ): Observable<Event> = AppClient.apiService.getEvent(eventId)
        .onErrorResumeNext {
            getEventsFromAssets(assets).map { events ->
                events.first { it.id == eventId }
            }
        }
        .map(EventDto::toDomain)
        .subscribeOn(Schedulers.io())

    private fun getEventsFromAssets(
        assets: AssetManager
    ): Observable<List<EventDto>> = Observable.fromCallable {
        return@fromCallable try {
            assets.open(EVENT_FILE).bufferedReader().use { inputStream ->
                gson.fromJson(inputStream, Array<EventDto>::class.java).toList()
            }
        } catch (e: Exception) {
            Log.e("CommonEventRepository-getEvents", "get events failed", e)
            emptyList()
        }
    }
}