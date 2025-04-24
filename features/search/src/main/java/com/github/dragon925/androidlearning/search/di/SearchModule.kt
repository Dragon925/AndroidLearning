package com.github.dragon925.androidlearning.search.di

import com.github.dragon925.androidlearning.core.api.domain.models.Event
import com.github.dragon925.androidlearning.core.api.domain.models.Mapper
import com.github.dragon925.androidlearning.search.ui.fragments.SearchByTypeFragment
import com.github.dragon925.androidlearning.search.ui.models.SearchResultItem
import com.github.dragon925.androidlearning.search.ui.models.toSearchResultItemBy
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntKey
import dagger.multibindings.IntoMap

@Module(includes = [SearchBindsModule::class])
internal object SearchModule {

    @Provides
    @[IntoMap IntKey(SearchByTypeFragment.SEARCH_BY_EVENT)]
    fun provideSearchByEventMapper(): Mapper<Event, SearchResultItem> {
        return Mapper { it.toSearchResultItemBy(Event::name) }
    }

    @Provides
    @[IntoMap IntKey(SearchByTypeFragment.SEARCH_BY_NKO)]
    fun provideSearchByOrganizerMapper(): Mapper<Event, SearchResultItem> {
        return Mapper { it.toSearchResultItemBy(Event::organizer) }
    }
}