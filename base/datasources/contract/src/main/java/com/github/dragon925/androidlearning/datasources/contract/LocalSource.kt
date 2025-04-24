package com.github.dragon925.androidlearning.datasources.contract

import kotlinx.coroutines.flow.Flow

sealed interface LocalSource<T: ModelContract> {

    fun load(): Flow<List<T>>

    suspend fun save(data: List<T>)

    interface Category: LocalSource<CategoryContract>

    interface Event: LocalSource<EventContract> {

        fun loadById(id: String): Flow<EventContract>

        fun loadReadIds(): Flow<List<String>>

        suspend fun read(vararg ids: String)
    }

}