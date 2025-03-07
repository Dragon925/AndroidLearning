package com.github.dragon925.androidlearning.common.data.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class DataLoadingService : Service() {

    companion object {
        const val RESULT_DATA = "DataLoadingService-resultData"
        const val RESULT_SUCCESS = 1
        const val RESULT_ERROR = 0
        const val RESULT_CANCELED = -1
    }

    private lateinit var executor: ExecutorService
    private val binder = DataBinder()

    @Volatile
    private var currentTask: Future<*>? = null

    @Volatile
    private var currentTaskName: String? = null

    override fun onCreate() {
        super.onCreate()
        executor = Executors.newSingleThreadExecutor()
    }

    override fun onBind(intent: Intent): IBinder = binder

    fun <T: Parcelable> loadData(resultReceiver: DataResultReceiver<T>, tag: String) {
        currentTask?.cancel(true)
        currentTaskName = tag
        currentTask = executor.submit {
            try {
                Thread.sleep(5000)

                val data = resultReceiver.loader()

                val bundle = Bundle().apply {
                    putParcelableArrayList(RESULT_DATA, ArrayList(data))
                }

                resultReceiver.send(RESULT_SUCCESS, bundle)
            } catch (e: InterruptedException) {
                resultReceiver.send(RESULT_CANCELED, null)
            } catch (e: Exception) {
                resultReceiver.send(RESULT_ERROR, null)
            }
        }
    }

    fun cancelLoadData(tag: String) {
        if (currentTaskName != tag) return
        currentTask?.cancel(true)
        currentTask = null
        currentTaskName = null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        currentTask?.cancel(true)
        currentTask = null
        currentTaskName = null
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdownNow()
    }

    inner class DataBinder : Binder() {
        val service get() = this@DataLoadingService
    }
}