package com.github.dragon925.androidlearning.news.utils

import android.app.Application
import android.content.Context
import com.github.dragon925.androidlearning.core.api.domain.repositories.CategoryRepository
import com.github.dragon925.androidlearning.core.api.domain.repositories.EventRepository
import com.github.dragon925.androidlearning.news.di.NewsDeps
import io.mockk.mockk

class TestApp : Application(), NewsDeps {

    override val context: Context
        get() = this

    override val categoryRepository: CategoryRepository
        get() = mockk(relaxed = true)

    override val eventRepository: EventRepository
        get() = mockk(relaxed = true)
}