package com.github.dragon925.androidlearning.news.ui.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import com.github.dragon925.androidlearning.news.ui.utils.DonationWorker
import com.github.dragon925.androidlearning.news.ui.utils.createOneTimeWorkRequest
import java.util.concurrent.TimeUnit

class RemindLaterReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_EVENT_ID = "event_id"
        const val EXTRA_EVENT_NAME = "event_name"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val id = intent?.getStringExtra(EXTRA_EVENT_ID) ?: return
        val name = intent.getStringExtra(EXTRA_EVENT_NAME) ?: return

        NotificationManagerCompat.from(context).cancel(id.hashCode())

        val workRequest = createOneTimeWorkRequest<DonationWorker>(
            DonationWorker.EVENT_ID to id,
            DonationWorker.EVENT_NAME to name,
            DonationWorker.WITH_REMINDER to false,
            addExtras = { setInitialDelay(30, TimeUnit.MINUTES) }
        )

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}