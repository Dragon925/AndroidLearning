package com.github.dragon925.androidlearning.search.di

import com.github.dragon925.androidlearning.core.api.domain.models.FeatureDeps
import com.github.dragon925.androidlearning.core.api.domain.repositories.EventRepository

interface SearchDeps : FeatureDeps {

    val eventRepository: EventRepository
}