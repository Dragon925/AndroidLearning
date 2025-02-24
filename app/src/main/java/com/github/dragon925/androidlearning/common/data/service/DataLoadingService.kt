package com.github.dragon925.androidlearning.common.data.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.os.ResultReceiver
import com.github.dragon925.androidlearning.common.data.repositories.CommonCategoryRepository
import com.github.dragon925.androidlearning.common.data.repositories.CommonEventRepository
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class DataLoadingService : Service() {

    companion object {
        const val EXTRA_EVENTS = "DataLoadingService-events"
        const val EXTRA_CATEGORY = "DataLoadingService-category"
        const val RESULT_SUCCESS = 1
        const val RESULT_ERROR = 0
        const val RESULT_CANCELED = -1
    }

    private lateinit var executor: ExecutorService
    private val binder = DataBinder()
    private lateinit var resultReceiver: ResultReceiver

    @Volatile
    private var currentTask: Future<*>? = null

    override fun onCreate() {
        super.onCreate()
        executor = Executors.newSingleThreadExecutor()
    }

    override fun onBind(intent: Intent): IBinder = binder

    fun loadData(resultReceiver: ResultReceiver) {
        this.resultReceiver = resultReceiver
        currentTask?.cancel(true)
        currentTask = executor.submit {
            try {
                Thread.sleep(5000)

                val events = CommonEventRepository.getEvents(assets)
                val categories = CommonCategoryRepository.getCategories(assets)

                val bundle = Bundle().apply {
                    putParcelableArrayList(EXTRA_EVENTS, ArrayList(events))
                    putParcelableArrayList(EXTRA_CATEGORY, ArrayList(categories))
                }

                sendResult(RESULT_SUCCESS, bundle)
            } catch (e: InterruptedException) {
                sendResult(RESULT_CANCELED)
            } catch (e: Exception) {
                sendResult(RESULT_ERROR)
            }
        }
    }

    private fun sendResult(resultCode: Int, resultData: Bundle? = null) {
        resultReceiver.send(resultCode, resultData)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        currentTask?.cancel(true)
        currentTask = null
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