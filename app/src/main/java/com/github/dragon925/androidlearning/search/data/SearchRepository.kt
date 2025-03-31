package com.github.dragon925.androidlearning.search.data

import com.github.dragon925.androidlearning.common.data.datasorces.local.AppDatabase
import com.github.dragon925.androidlearning.common.data.repositories.CommonEventRepository
import kotlinx.coroutines.flow.map

object SearchRepository {

    fun searchEvents(
        keywords: List<String>,
        db: AppDatabase
    ) = CommonEventRepository.getEvents(db)
        .map { events ->
            events.filter { event ->
                keywords.any { event.name.contains(it, true) }
            }
        }

    fun searchOrganizers(
        keywords: List<String>,
        db: AppDatabase
    ) = CommonEventRepository.getEvents(db)
        .map { events ->
            events.filter { event ->
                keywords.any { event.organizer.contains(it, true) }
            }
            .distinctBy { it.organizer }
        }
}