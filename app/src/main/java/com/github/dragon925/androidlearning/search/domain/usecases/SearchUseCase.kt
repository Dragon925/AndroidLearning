package com.github.dragon925.androidlearning.search.domain.usecases

import com.github.dragon925.androidlearning.common.domain.models.Event
import kotlinx.coroutines.flow.Flow

fun interface SearchUseCase {

    fun search(keywords: List<String>): Flow<List<Event>>
}