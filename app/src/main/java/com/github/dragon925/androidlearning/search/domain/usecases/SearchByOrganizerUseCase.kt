package com.github.dragon925.androidlearning.search.domain.usecases

import com.github.dragon925.androidlearning.common.domain.repositories.EventRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.map

class SearchByOrganizerUseCase @Inject constructor(
    private val repository: EventRepository
) : SearchUseCase {

    override fun search(keywords: List<String>) = repository.getEvents()
        .map { events ->
            events.filter { event ->
                keywords.any { event.organizer.contains(it, true) }
            }
                .distinctBy { it.organizer }
        }
}