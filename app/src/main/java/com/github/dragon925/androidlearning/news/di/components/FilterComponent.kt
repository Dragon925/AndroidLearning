package com.github.dragon925.androidlearning.news.di.components

import com.github.dragon925.androidlearning.news.di.modules.FiltersModule
import com.github.dragon925.androidlearning.news.di.scopes.FiltersScope
import com.github.dragon925.androidlearning.news.ui.fragments.FilterFragment
import dagger.Subcomponent

@Subcomponent(modules = [FiltersModule::class])
@FiltersScope
fun interface FilterComponent {

    fun inject(filterFragment: FilterFragment)

    @Subcomponent.Builder
    fun interface Builder {

        fun build(): FilterComponent
    }
}