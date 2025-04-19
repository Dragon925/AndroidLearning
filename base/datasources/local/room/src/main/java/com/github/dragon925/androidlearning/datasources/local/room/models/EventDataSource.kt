package com.github.dragon925.androidlearning.datasources.local.room.models

import com.github.dragon925.androidlearning.datasources.contract.EventContract
import com.github.dragon925.androidlearning.datasources.contract.LocalSource
import com.github.dragon925.androidlearning.datasources.local.room.dao.EventDao
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class EventDataSource @Inject constructor (
    private val eventDao: EventDao
) : LocalSource.Event {

    override fun load(): Flow<List<EventContract>> = eventDao.loadEvents()

    override suspend fun save(data: List<EventContract>) = eventDao.saveEvents(data)

    override fun loadById(id: String): Flow<EventContract> = eventDao.loadEvent(id)

    override fun loadReadIds(): Flow<List<String>> = eventDao.loadReadEventIds()

    override suspend fun read(vararg ids: String) = eventDao.readEvents(*ids)
}