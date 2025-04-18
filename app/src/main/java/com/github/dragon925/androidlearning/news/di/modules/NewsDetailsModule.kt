package com.github.dragon925.androidlearning.news.di.modules

import android.content.Context
import com.github.dragon925.androidlearning.common.contract.Mapper
import com.github.dragon925.androidlearning.common.domain.models.Event
import com.github.dragon925.androidlearning.news.ui.models.NewsDetailItem
import com.github.dragon925.androidlearning.news.ui.utils.toNewsDetailItem
import dagger.Module
import dagger.Provides

@Module
object NewsDetailsModule {

    @Provides
    fun provideNewsDetailsMapper(context: Context): Mapper<Event, NewsDetailItem> {
        return Mapper { it.toNewsDetailItem(context) }
    }
}