package com.github.dragon925.androidlearning.search.di

import com.github.dragon925.androidlearning.search.domain.usecases.SearchByEventsUseCase
import com.github.dragon925.androidlearning.search.domain.usecases.SearchByOrganizerUseCase
import com.github.dragon925.androidlearning.search.domain.usecases.SearchUseCase
import com.github.dragon925.androidlearning.search.ui.fragments.SearchByTypeFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntKey
import dagger.multibindings.IntoMap

@Module
interface SearchBindsModule {

    @Binds
    @[IntoMap IntKey(SearchByTypeFragment.SEARCH_BY_EVENT)]
    fun bindSearchByEventUseCase(useCase: SearchByEventsUseCase): SearchUseCase

    @Binds
    @[IntoMap IntKey(SearchByTypeFragment.SEARCH_BY_NKO)]
    fun bindSearchByOrganizerUseCase(useCase: SearchByOrganizerUseCase): SearchUseCase
}