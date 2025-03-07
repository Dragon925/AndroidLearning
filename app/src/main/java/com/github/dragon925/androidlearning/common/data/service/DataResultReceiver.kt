package com.github.dragon925.androidlearning.common.data.service

import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.os.ResultReceiver
import androidx.core.os.BundleCompat

class DataResultReceiver<T : Parcelable>(
    handler: Handler,
    private val clazz: Class<T>,
    private val onSuccess: OnSuccess<T>,
    private val onError: OnError,
    private val onCancel: OnCancel? = null,
    val loader: () -> List<T>
) : ResultReceiver(handler) {

    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        when (resultCode) {
            DataLoadingService.RESULT_SUCCESS -> {
                val data = resultData?.let {
                    BundleCompat.getParcelableArrayList(it, DataLoadingService.RESULT_DATA, clazz)
                } ?: emptyList()
                onSuccess(data)
            }
            DataLoadingService.RESULT_ERROR -> onError()
            DataLoadingService.RESULT_CANCELED -> onCancel?.invoke()
        }
    }


    sealed interface OnResultAction<out T : Parcelable>

    fun interface OnSuccess<T : Parcelable> : OnResultAction<T> {
        operator fun invoke(data: List<T>)
    }

    fun interface OnError : OnResultAction<Nothing> {
        operator fun invoke()
    }

    fun interface OnCancel : OnResultAction<Nothing> {
        operator fun invoke()
    }
}