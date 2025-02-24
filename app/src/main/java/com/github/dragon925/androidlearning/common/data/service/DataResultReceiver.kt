package com.github.dragon925.androidlearning.common.data.service

import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import com.github.dragon925.androidlearning.common.domain.Category
import com.github.dragon925.androidlearning.common.domain.Event
import com.github.dragon925.androidlearning.common.ui.readParcelableArrayList

class DataResultReceiver(
    handler: Handler,
    private val onSuccess: OnSuccess,
    private val onError: OnError,
    private val onCancel: OnCancel? = null
) : ResultReceiver(handler) {

    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        when (resultCode) {
            DataLoadingService.RESULT_SUCCESS -> {
                val events = resultData
                    ?.readParcelableArrayList<Event>(DataLoadingService.EXTRA_EVENTS)
                    ?: emptyList()
                val categories = resultData
                    ?.readParcelableArrayList<Category>(DataLoadingService.EXTRA_CATEGORY)
                    ?: emptyList()
                onSuccess(events, categories)
            }
            DataLoadingService.RESULT_ERROR -> onError()
            DataLoadingService.RESULT_CANCELED -> onCancel?.invoke()
        }
    }


    sealed interface OnResultAction

    fun interface OnSuccess : OnResultAction {
        operator fun invoke(events: List<Event>, categories: List<Category>)
    }

    fun interface OnError : OnResultAction {
        operator fun invoke()
    }

    fun interface OnCancel : OnResultAction {
        operator fun invoke()
    }
}