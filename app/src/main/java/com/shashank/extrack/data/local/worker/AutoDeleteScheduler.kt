package com.shashank.extrack.data.local.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object AutoDeleteScheduler {
    private const val WORK_NAME = "auto_delete_work"

    fun scheduleDaily(context: Context) {
        val request = PeriodicWorkRequestBuilder<AutoDeleteWorker>(1, TimeUnit.DAYS).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }
}