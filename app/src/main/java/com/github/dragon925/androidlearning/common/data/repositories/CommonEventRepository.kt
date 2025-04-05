package com.github.dragon925.androidlearning.common.data.repositories

import android.util.Log
import com.github.dragon925.androidlearning.common.data.datasorces.local.AppDatabase
import com.github.dragon925.androidlearning.common.data.datasorces.remote.AppClient
import com.github.dragon925.androidlearning.common.data.models.EventData
import com.github.dragon925.androidlearning.common.data.toDomain
import com.github.dragon925.androidlearning.common.domain.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

object CommonEventRepository {

    @Volatile
    private var isLoaded = false

    fun getEvents(db: AppDatabase): Flow<List<Event>> = db.eventDao().loadEvents()
        .onStart {
            if (isLoaded) return@onStart

            CommonCategoryRepository.preloadCategories(db)
            try {
                val events = AppClient.apiService.getEvents()
                db.eventDao().saveEvents(events)
                isLoaded = true
            } catch (e: Exception) {
                Log.d("EventRepository", "loading events failed", e)
            }
        }
        .map { events ->
            events.map(EventData::toDomain)
                .sortedWith(
                    compareBy<Event> { it.startDate }
                        .thenBy { it.endDate }
                        .thenBy { it.name }
                )
        }
        .flowOn(Dispatchers.IO)

    fun getEventById(
        eventId: String,
        db: AppDatabase
    ): Flow<Event> = db.eventDao().loadEvent(eventId)
        .map(EventData::toDomain)
        .flowOn(Dispatchers.IO)

    suspend fun readEvents(db: AppDatabase, vararg eventIds: String) {
        db.eventDao().readEvents(*eventIds)
    }

    fun getReadEventIds(db: AppDatabase): Flow<Set<String>> = db.eventDao().loadReadEventIds()
        .map { it.toSet() }
}