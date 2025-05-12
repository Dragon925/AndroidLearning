package com.github.dragon925.androidlearning.news.ui.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.github.dragon925.androidlearning.news.R
import com.github.dragon925.androidlearning.news.ui.activities.NewsDetailsActivity
import com.github.dragon925.androidlearning.news.ui.receivers.RemindLaterReceiver

class DonationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    companion object {
        const val EVENT_ID = "event_id"
        const val EVENT_NAME = "event_name"
        const val AMOUNT = "amount"
        const val WITH_REMINDER = "with_reminder"

        private const val CHANNEL_ID = "com.github.dragon925.androidlearning.news.channel.donations"
    }

    override fun doWork(): Result {
        val eventId = inputData.getString(EVENT_ID) ?: return Result.failure()
        val eventName = inputData.getString(EVENT_NAME) ?: return Result.failure()
        val donationAmount = inputData.getInt(AMOUNT, 0)
        val withReminder = inputData.getBoolean(WITH_REMINDER, true)

        sendNotification(eventId, eventName, donationAmount, withReminder)

        return Result.success()
    }

    private fun sendNotification(
        eventId: String,
        eventName: String,
        amount: Int,
        withReminder: Boolean = true
    ) {
        val context = applicationContext
        val notificationManager = NotificationManagerCompat.from(context)
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.resources.getString(R.string.donations),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notificationBuilder = createNotificationBuilder(
            context, eventId, eventName, amount, withReminder
        )

        if (withReminder) {
            notificationBuilder.addAction(createRemindAction(context, eventId, eventName))
        }

        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(
                eventId.hashCode(), notificationBuilder.build())
        }
    }

    private fun createNotificationBuilder(
        context: Context,
        eventId: String,
        eventName: String,
        amount: Int = 0,
        withReminder: Boolean = false
    ) = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.icon_logo)
        .setContentTitle(eventName)
        .setContentText(
            if (withReminder) {
                context.resources.getString(R.string.donation_gratitude_notification, amount)
            } else {
                context.resources.getString(R.string.donation_gratitude_notification_remind)
            }
        )
        .setAutoCancel(true)
        .setContentIntent(createContentIntent(context, eventId, eventName))

    private fun createContentIntent(
        context: Context,
        eventId: String,
        eventName: String
    ): PendingIntent {
        val uri = Uri.Builder()
            .scheme("wanthelp")
            .authority("news")
            .build()

        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            `package` = context.packageName
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtras(
                bundleOf(
                    NewsDetailsActivity.EXTRA_NEWS_ID to eventId,
                    NewsDetailsActivity.EXTRA_NEWS_TITLE to eventName
                )
            )
        }

        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createRemindAction(
        context: Context,
        eventId: String,
        eventName: String,
    ): NotificationCompat.Action {

        val intent = Intent(context, RemindLaterReceiver::class.java).apply {
            putExtra(RemindLaterReceiver.EXTRA_EVENT_ID, eventId)
            putExtra(RemindLaterReceiver.EXTRA_EVENT_NAME, eventName)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Action.Builder(
            R.drawable.ic_remind,
            context.resources.getString(R.string.remind_later),
            pendingIntent
        )
            .build()
    }
}