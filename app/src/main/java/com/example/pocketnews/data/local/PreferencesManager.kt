package com.example.pocketnews.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {

    companion object{
        private val CATEGORY_KEY = stringPreferencesKey("selected_category")
        private val UPDATE_INTERVAL_HOURS = intPreferencesKey("update_interval")
        private val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
        private val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        private val DARK_MODE_ENABLED = booleanPreferencesKey("dark_mode_enabled")
    }

    suspend fun saveNotificationsToDataStore(enabled: Boolean){
        context.dataStore.edit { settings ->
            settings[NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun saveCategoryToDataStore(categoryName: String){
        context.dataStore.edit { settings ->
            settings[CATEGORY_KEY] = categoryName
        }
    }

    suspend fun saveHoursToDataStore(updateHours: Int){
        context.dataStore.edit { settings ->
            settings[UPDATE_INTERVAL_HOURS] = updateHours
        }
    }

    suspend fun saveLaunchToDataStore(launch: Boolean){
        context.dataStore.edit { settings ->
            settings[IS_FIRST_LAUNCH] = launch
        }
    }

    suspend fun saveDarkModeToDataStore(enabled: Boolean){
        context.dataStore.edit { settings ->
            settings[DARK_MODE_ENABLED] = enabled
        }
    }

    val notificationsFlow: Flow<Boolean> = context.dataStore.data.map { settings ->
        settings[NOTIFICATIONS_ENABLED] ?: false
    }

    val categoryFlow: Flow<String> = context.dataStore.data.map { settings ->
        settings[CATEGORY_KEY] ?: "General"
    }

    val updatehoursflow: Flow<Int> = context.dataStore.data.map { settings ->
        settings[UPDATE_INTERVAL_HOURS] ?: 1
    }

    val isfirstlaunchflow: Flow<Boolean> = context.dataStore.data.map { settings ->
        settings[IS_FIRST_LAUNCH] ?: true
    }

    val darkModeFlow: Flow<Boolean> = context.dataStore.data.map { settings ->
        settings[DARK_MODE_ENABLED] ?: false
    }
}