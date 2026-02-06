package com.example.pocketnews.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object NewsWorkManager {

    fun scheduleNewsCheck(context: Context,updateIntervalHours: Int){

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<NewsCheckWorker>(
            repeatInterval = updateIntervalHours.toLong(),
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "news_check_work",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
}
