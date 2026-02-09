package com.example.pocketnews

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.pocketnews.utils.NotificationHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class PocketNewsApp : Application(), Configuration.Provider{

    @Inject
    lateinit var  workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        val notificationHelper = NotificationHelper(this)
        notificationHelper.createNotificationChannel()
        Log.d("PocketNewsApp", "Notification channel created")
    }
}