package com.github.dragon925.androidlearning.core.di

import android.content.Context
import com.github.dragon925.androidlearning.core.api.domain.repositories.CategoryRepository
import com.github.dragon925.androidlearning.core.api.domain.repositories.EventRepository
import dagger.BindsInstance
import dagger.Component

@Component(modules = [CoreModule::class])
@CoreScope
interface CoreComponent {

    val categoryRepository: CategoryRepository
    val eventRepository: EventRepository

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): CoreComponent
    }
}