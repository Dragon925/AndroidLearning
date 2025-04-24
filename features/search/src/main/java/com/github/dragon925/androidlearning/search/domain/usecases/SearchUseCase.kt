package com.github.dragon925.androidlearning.search.domain.usecases

import com.github.dragon925.androidlearning.core.api.domain.models.Event
import kotlinx.coroutines.flow.Flow

internal fun interface SearchUseCase {

    fun search(keywords: List<String>): Flow<List<Event>>
}