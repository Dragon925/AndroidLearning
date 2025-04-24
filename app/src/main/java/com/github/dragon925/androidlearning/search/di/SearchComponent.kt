package com.github.dragon925.androidlearning.search.di

import com.github.dragon925.androidlearning.search.ui.fragments.SearchByTypeFragment
import dagger.Subcomponent


@Subcomponent(modules = [SearchModule::class])
@SearchScope
fun interface SearchComponent {

    fun inject(searchByTypeFragment: SearchByTypeFragment)

    @Subcomponent.Builder
    fun interface Builder {
        fun build(): SearchComponent
    }
}