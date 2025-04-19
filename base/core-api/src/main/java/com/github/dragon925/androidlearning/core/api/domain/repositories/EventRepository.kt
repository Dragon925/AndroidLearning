package com.github.dragon925.androidlearning.core.api.domain.repositories

import com.github.dragon925.androidlearning.core.api.domain.models.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

interface EventRepository {

    fun getEvents(): Flow<List<Event>>

    fun getEventById(eventId: String): Flow<Event>

    suspend fun readEvents(
        vararg eventIds: String,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    )

    fun getReadEventIds(): Flow<Set<String>>
}