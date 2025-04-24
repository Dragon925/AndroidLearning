package com.github.dragon925.androidlearning.common.data.datasorces.local.models

import com.github.dragon925.androidlearning.common.contract.CategoryContract
import com.github.dragon925.androidlearning.common.contract.EventContract
import com.github.dragon925.androidlearning.common.contract.ModelContract
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