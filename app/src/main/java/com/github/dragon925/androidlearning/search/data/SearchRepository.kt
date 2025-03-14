package com.github.dragon925.androidlearning.search.data

import android.content.res.AssetManager
import com.github.dragon925.androidlearning.common.data.repositories.CommonEventRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

object SearchRepository {

    fun searchEvents(
        keywords: List<String>,
        assets: AssetManager
    ) = flow {
        delay(5000)

        val events =  CommonEventRepository.getEvents(assets)
            .filter { event -> keywords.any { event.name.contains(it, true) } }
        emit(events)
    }

    fun searchOrganizers(
        keywords: List<String>,
        assets: AssetManager
    ) = flow {
        delay(5000)

        val events =  CommonEventRepository.getEvents(assets)
            .filter { event -> keywords.any { event.organizer.contains(it, true) } }
            .distinctBy { it.organizer }
        emit(events)
    }
}