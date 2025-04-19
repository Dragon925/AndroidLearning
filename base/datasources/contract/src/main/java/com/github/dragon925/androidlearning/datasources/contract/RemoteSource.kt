package com.github.dragon925.androidlearning.datasources.contract


fun interface RemoteSource<T : ModelContract> {

    suspend fun load(): List<T>
}