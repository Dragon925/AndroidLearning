package com.github.dragon925.androidlearning.common.data.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.github.dragon925.androidlearning.common.data.service.DataResultReceiver.*

class DataLoadingServiceHelper(
    private val onSuccess: OnSuccess,
    private val onError: OnError,
    private val onCancel: OnCancel? = null
) {

    @Volatile
    var isLoading = false
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
        val receiver = DataResultReceiver(
            handler = Handler(Looper.getMainLooper()),
            onSuccess = onSuccess.runBefore { isLoading = false },
            onError = onError.runBefore { isLoading = false },
            onCancel = onCancel?.runBefore { isLoading = false }
        )

        service?.loadData(receiver)
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
            isLoading = false
            isBound = false
            service = null
        }
    }

    private fun OnSuccess.runBefore(block: () -> Unit): OnSuccess {
        return OnSuccess { events, categories ->
            block()
            this(events, categories)
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