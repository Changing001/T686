package com.announcement.comprehension.nav

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class NmStamp(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        runCatching {
            val request =
                OneTimeWorkRequestBuilder<NmStamp>().setInitialDelay(36 + 8L, TimeUnit.SECONDS)
                    .build()
            WorkManager.Companion.getInstance(applicationContext)
                .enqueueUniqueWork("NmStamp", ExistingWorkPolicy.REPLACE, request)
        }
        return Result.success()
    }
}