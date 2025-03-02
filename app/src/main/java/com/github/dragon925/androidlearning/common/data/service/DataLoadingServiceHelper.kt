package com.github.dragon925.androidlearning.common.data.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Parcelable
import com.github.dragon925.androidlearning.common.data.service.DataResultReceiver.*

class DataLoadingServiceHelper<T: Parcelable>(
    private val clazz: Class<T>,
    private val onSuccess: OnSuccess<T>,
    private val onError: OnError,
    private val onCancel: OnCancel? = null,
    private val loader: () -> List<T>
) {

    @Volatile
    var isLoading = false
        private set

    @Volatile
    var isDone = false
        private set

    private var isBound = false
    private var service: DataLoadingService? = null
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val dataBinder = binder as DataLoadingService.DataBinder
            service = dataBinder.service
            loadData()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isLoading = false
            isBound = false
            service = null
        }
    }

    private fun loadData() {
        isLoading = true
        isDone = false
        val receiver = DataResultReceiver(
            handler = Handler(Looper.getMainLooper()),
            clazz = clazz,
            onSuccess = onSuccess.runBefore {
                isLoading = false
                isDone = true
            },
            onError = onError.runBefore { isLoading = false },
            onCancel = onCancel?.runBefore { isLoading = false },
            loader = loader
        )

        service?.loadData(receiver, clazz.name)
    }

    fun reload() {
        if (isBound) {
            loadData()
        }
    }

    fun bindService(context: Context) {
        context.bindService(
            Intent(context, DataLoadingService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
        isBound = true
    }

    fun unbindService(context: Context) {
        if (isBound) {
            context.unbindService(connection)
            service?.cancelLoadData(clazz.name)
            isLoading = false
            isBound = false
            service = null
        }
    }

    private fun OnSuccess<T>.runBefore(block: () -> Unit): OnSuccess<T> {
        return OnSuccess { data ->
            block()
            this(data)
        }
    }

    private fun OnError.runBefore(block: () -> Unit): OnError {
        return OnError {
            block()
            this()
        }
    }

    private fun OnCancel.runBefore(block: () -> Unit): OnCancel {
        return OnCancel {
            block()
            this()
        }
    }
}