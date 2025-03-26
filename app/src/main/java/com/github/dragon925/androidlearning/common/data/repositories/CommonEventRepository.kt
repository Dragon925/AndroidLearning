package com.github.dragon925.androidlearning.common.data.repositories

import android.content.res.AssetManager
import android.util.Log
import com.github.dragon925.androidlearning.common.data.datasorces.remote.AppClient
import com.github.dragon925.androidlearning.common.data.models.EventDto
import com.github.dragon925.androidlearning.common.data.toDomain
import com.github.dragon925.androidlearning.common.domain.Event
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

object CommonEventRepository {

    private const val EVENT_FILE = "events.json"

    private val gson = Gson()

    fun getEvents(
        assets: AssetManager
    ): Flow<List<Event>> = flow {
        emit(AppClient.apiService.getEvents())
    }
        .catch { emit(getEventsFromAssets(assets)) }
        .map { events ->
            events.map(EventDto::toDomain)
                .sortedWith(
                    compareBy<Event> { it.startDate }
                        .thenBy { it.endDate }
                        .thenBy { it.name }
                )
        }
        .flowOn(Dispatchers.IO)

    fun getEventById(
        eventId: String,
        assets: AssetManager
    ): Flow<Event> = flow {
        emit(AppClient.apiService.getEvent(eventId))
    }
        .catch {
            emit(getEventsFromAssets(assets).first { it.id == eventId })
        }
        .map(EventDto::toDomain)
        .flowOn(Dispatchers.IO)

    private suspend fun getEventsFromAssets(
        assets: AssetManager,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): List<EventDto> = withContext(dispatcher) {
        return@withContext try {
            assets.open(EVENT_FILE).bufferedReader().use { inputStream ->
                gson.fromJson(inputStream, Array<EventDto>::class.java).toList()
            }
        } catch (e: Exception) {
            Log.e("CommonEventRepository-getEvents", "get events failed", e)
            emptyList()
        }
    }
}