package com.github.dragon925.androidlearning.news.di.modules

import android.content.Context
import com.github.dragon925.androidlearning.common.contract.Mapper
import com.github.dragon925.androidlearning.common.domain.models.Event
import com.github.dragon925.androidlearning.news.di.components.FilterComponent
import com.github.dragon925.androidlearning.news.ui.models.NewsItem
import com.github.dragon925.androidlearning.news.ui.utils.toNewsItem
import dagger.Module
import dagger.Provides

@Module(
    includes = [NewsListModule::class],
    subcomponents = [FilterComponent::class]
)
object NewsModule {

    @Provides
    fun provideNewsItemMapper(context: Context): Mapper<Event, NewsItem> {
        return Mapper { it.toNewsItem(context) }
    }
}