package com.announcement.comprehension.t

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class TWorkManager(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        runCatching {
            val request = OneTimeWorkRequestBuilder<TWorkManager>().setInitialDelay(48L, TimeUnit.SECONDS).build()
            WorkManager.Companion.getInstance(applicationContext).enqueueUniqueWork("TWorkManager", ExistingWorkPolicy.REPLACE, request)
        }
        return Result.success()
    }
}