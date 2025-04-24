package com.github.dragon925.androidlearning.core.repositories

import android.util.Log
import com.github.dragon925.androidlearning.core.api.domain.models.Event
import com.github.dragon925.androidlearning.core.api.domain.repositories.EventRepository
import com.github.dragon925.androidlearning.core.toDomain
import com.github.dragon925.androidlearning.datasources.contract.EventContract
import com.github.dragon925.androidlearning.datasources.contract.LocalSource
import com.github.dragon925.androidlearning.datasources.contract.RemoteSource
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

internal class EventRepositoryImpl @Inject constructor(
    private val remoteSource: RemoteSource<EventContract>,
    private val localSource: LocalSource.Event,
    private val categoryPreloader: CategoryPreloader
) : EventRepository {

    companion object {
        @Volatile
        private var isLoaded = false
    }

    private suspend fun preloadEvents() {
        if (isLoaded) return

        categoryPreloader.preload()

        try {
            val events = remoteSource.load()
            localSource.save(events)
            isLoaded = true
        } catch (e: Exception) {
            Log.d("EventRepository", "loading events failed", e)
        }
    }

    override fun getEvents(): Flow<List<Event>> = localSource.load()
        .onStart {
            preloadEvents()
        }
        .map { events ->
            events.map(EventContract::toDomain)
                .sortedWith(
                    compareBy(Event::startDate)
                        .thenBy(Event::endDate)
                        .thenBy(Event::name)
                )
        }
        .flowOn(Dispatchers.IO)

    override fun getEventById(eventId: String): Flow<Event> = localSource.loadById(eventId)
        .map(EventContract::toDomain)
        .flowOn(Dispatchers.IO)

    override suspend fun readEvents(
        vararg eventIds: String,
        dispatcher: CoroutineDispatcher
    ) = withContext(dispatcher) {
        localSource.read(*eventIds)
    }

    override fun getReadEventIds(): Flow<Set<String>> = localSource.loadReadIds()
        .map { it.toSet() }
        .flowOn(Dispatchers.IO)
}