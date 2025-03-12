package com.github.dragon925.androidlearning.search.data

import android.content.res.AssetManager
import com.github.dragon925.androidlearning.common.data.repositories.CommonEventRepository
import com.github.dragon925.androidlearning.common.domain.Event
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

object SearchRepository {

    fun searchEvents(
        keywords: List<String>,
        assets: AssetManager
    ) = Observable.fromCallable<List<Event>> {
        try {
            Thread.sleep(5000)
        } catch (e: InterruptedException) {
            return@fromCallable emptyList()
        }

        return@fromCallable CommonEventRepository.getEvents(assets)
            .filter { event -> keywords.any { event.name.contains(it, true) } }
    }.subscribeOn(Schedulers.io())

    fun searchOrganizers(
        keywords: List<String>,
        assets: AssetManager
    ) = Observable.fromCallable<List<Event>> {
        try {
            Thread.sleep(5000)
        } catch (e: InterruptedException) {
            return@fromCallable emptyList()
        }

        return@fromCallable CommonEventRepository.getEvents(assets)
            .filter { event -> keywords.any { event.organizer.contains(it, true) } }
            .distinctBy { it.organizer }
    }.subscribeOn(Schedulers.io())
}