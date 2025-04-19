package com.github.dragon925.androidlearning.search.di

import com.github.dragon925.androidlearning.search.ui.fragments.SearchByTypeFragment
import dagger.Component


@Component(modules = [SearchModule::class], dependencies = [SearchDeps::class])
@SearchScope
internal fun interface SearchComponent {

    fun inject(searchByTypeFragment: SearchByTypeFragment)

    @Component.Builder
    interface Builder {

        fun deps(deps: SearchDeps): Builder

        fun build(): SearchComponent
    }
}