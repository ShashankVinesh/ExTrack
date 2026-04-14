package com.shashank.extrack.data.local.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shashank.extrack.data.local.db.TransactionDatabase
import com.shashank.extrack.data.local.repository.TransactionRepository
import com.shashank.extrack.ui.settings.AutoDeletePolicy
import com.shashank.extrack.ui.settings.AutoDeletePrefs
import kotlinx.coroutines.flow.first

class AutoDeleteWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val prefs = AutoDeletePrefs(applicationContext)
        val policy = prefs.policyFlow.first()

        if (policy == AutoDeletePolicy.NEVER) return Result.success()

        val now = System.currentTimeMillis()
        val cutoff = when (policy) {
            AutoDeletePolicy.DAY -> now - 86400000L
            AutoDeletePolicy.WEEK -> now - 7 * 86400000L
            AutoDeletePolicy.MONTH -> now - 30L * 86400000L
            AutoDeletePolicy.NEVER -> now
        }

        val dao = TransactionDatabase.getDatabase(applicationContext).transactionDao()
        val repo = TransactionRepository(dao)

        repo.softDeleteOlderThan(cutoff = cutoff, deletedAt = now)
        return Result.success()
    }
}