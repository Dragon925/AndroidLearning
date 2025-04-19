package com.github.dragon925.androidlearning.news.di

import android.content.Context
import com.github.dragon925.androidlearning.core.api.domain.models.FeatureDeps
import com.github.dragon925.androidlearning.core.api.domain.repositories.CategoryRepository
import com.github.dragon925.androidlearning.core.api.domain.repositories.EventRepository

interface NewsDeps : FeatureDeps {
    val context: Context
    val categoryRepository: CategoryRepository
    val eventRepository: EventRepository
}