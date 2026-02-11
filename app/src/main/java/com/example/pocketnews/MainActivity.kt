package com.example.pocketnews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.pocketnews.data.local.PreferencesManager
import com.example.pocketnews.presentation.Navigation
import com.example.pocketnews.ui.theme.PocketNewsTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
}

