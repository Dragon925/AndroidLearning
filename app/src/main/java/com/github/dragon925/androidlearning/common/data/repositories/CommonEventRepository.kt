package com.github.dragon925.androidlearning.common.data.repositories

import android.content.res.AssetManager
import com.github.dragon925.androidlearning.common.data.datasorces.remote.AppClient
import com.github.dragon925.androidlearning.common.data.models.EventDto
import com.github.dragon925.androidlearning.common.data.toDomain
import com.github.dragon925.androidlearning.common.domain.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

object CommonEventRepository {

    private const val EVENT_FILE = "events.json"

    private val helper = LoadHelper(
        fileName = EVENT_FILE,
        jsonClass = Array<EventDto>::class.java
    )

    fun getEvents(
        assets: AssetManager
    ): Flow<List<Event>> = helper.loadData(
        assets = assets,
        loader = AppClient.apiService::getEvents
    ) { events ->
        events.map(EventDto::toDomain)
            .sortedWith(
                compareBy(Event::startDate)
                    .thenBy(Event::endDate)
                    .thenBy(Event::name)
            )
    }
        .flowOn(Dispatchers.IO)

    fun getEventById(
        eventId: String,
        assets: AssetManager
    ): Flow<Event> = helper.loadFirstData(
        assets = assets,
        loader = {
            AppClient.apiService.getEvent(eventId)
        },
        mapper = EventDto::toDomain,
        predicate = { event ->
            event.id == eventId
        }
    )
        .flowOn(Dispatchers.IO)
}