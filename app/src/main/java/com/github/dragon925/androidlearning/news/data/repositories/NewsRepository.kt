package com.github.dragon925.androidlearning.news.data.repositories

import com.github.dragon925.androidlearning.common.data.datasorces.local.AppDatabase
import com.github.dragon925.androidlearning.common.data.repositories.CommonEventRepository
import kotlinx.coroutines.rx3.asObservable

class NewsRepository(
    private val database: AppDatabase,
) {

    fun getNews() = CommonEventRepository.getEvents(database).asObservable()

    fun getReadNewsIds() = CommonEventRepository.getReadEventIds(database).asObservable()

    suspend fun readNews(vararg ids: String) {
        CommonEventRepository.readEvents(database, *ids)
    }
}