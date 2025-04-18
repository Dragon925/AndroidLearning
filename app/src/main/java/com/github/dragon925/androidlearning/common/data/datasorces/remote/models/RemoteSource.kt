package com.github.dragon925.androidlearning.common.data.datasorces.remote.models

import com.github.dragon925.androidlearning.common.contract.ModelContract

fun interface RemoteSource<T : ModelContract> {

    suspend fun load(): List<T>
}