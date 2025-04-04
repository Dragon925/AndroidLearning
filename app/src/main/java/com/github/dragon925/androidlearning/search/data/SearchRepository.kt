package com.github.dragon925.androidlearning.search.data

import android.content.res.AssetManager
import com.github.dragon925.androidlearning.common.data.repositories.CommonEventRepository
import kotlinx.coroutines.rx3.asFlow

object SearchRepository {

    fun searchEvents(
        keywords: List<String>,
        assets: AssetManager
    ) = CommonEventRepository.getEvents(assets)
        .map { events ->
            events.filter { event ->
                keywords.any { event.name.contains(it, true) }
            }
        }.asFlow()

    fun searchOrganizers(
        keywords: List<String>,
        assets: AssetManager
    ) = CommonEventRepository.getEvents(assets)
        .map { events ->
            events.filter { event ->
                keywords.any { event.organizer.contains(it, true) }
            }
            .distinctBy { it.organizer }
        }.asFlow()
}