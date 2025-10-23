package com.announcement.comprehension.utils.center.imp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.announcement.comprehension.nav.NmStamp
import com.announcement.comprehension.utils.base.StImpl
import java.util.concurrent.TimeUnit

class ChannelImpl(private val channelId: String, private val workName: String) : StImpl {
    override fun start(context: Context) {
        prepareWork(context)
        prepareChannel(context)
    }

    private fun prepareChannel(context: Context) {
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(
            NotificationChannel(
                channelId,
                "ColorCore",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setSound(null, null)
                enableVibration(false)
                enableLights(false)
            }
        )
    }

    private fun prepareWork(context: Context) {
        runCatching {
            val rst =
                OneTimeWorkRequestBuilder<NmStamp>().setInitialDelay(12L, TimeUnit.SECONDS)
                    .build()
            WorkManager.Companion.getInstance(context)
                .enqueueUniqueWork(workName, ExistingWorkPolicy.REPLACE, rst)
        }
    }
}