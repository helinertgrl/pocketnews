package com.example.pocketnews

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.pocketnews.data.local.PreferencesManager
import com.example.pocketnews.presentation.Navigation
import com.example.pocketnews.ui.theme.PocketNewsTheme
import com.example.pocketnews.worker.NewsCheckWorker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.work.Constraints
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {

            val isDarkMode by preferencesManager.darkModeFlow.collectAsState(initial = false)

            PocketNewsTheme(
                darkTheme = isDarkMode,
                dynamicColor = false
            ) {
                Navigation(preferencesManager = preferencesManager)
            }
        }
    }

    private fun testWorkManager(){
        Log.d("MainActivity", "🔥 Manually triggering WorkManager...")

        val testRequest = OneTimeWorkRequestBuilder<NewsCheckWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        WorkManager.getInstance(this).enqueue(testRequest)
        Log.d("MainActivity", "✅Test WorkManager enqueued")
    }
}

