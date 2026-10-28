package com.example.gpstracking.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.gpstracking.domain.model.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

class SettingsDataStore(private val context: Context) {

    private val keyUpdateInterval = intPreferencesKey("location_update_interval")
    private val keyBackgroundTracking = booleanPreferencesKey("background_tracking")

    val settingsFlow: Flow<Settings> = context.dataStore.data.map { prefs ->
        Settings(
            updateIntervalSeconds = prefs[keyUpdateInterval] ?: Settings.DEFAULT_INTERVAL,
            backgroundTracking = prefs[keyBackgroundTracking] ?: false
        )
    }

    suspend fun setUpdateInterval(seconds: Int) {
        context.dataStore.edit { it[keyUpdateInterval] = seconds }
    }

    suspend fun setBackgroundTracking(enabled: Boolean) {
        context.dataStore.edit { it[keyBackgroundTracking] = enabled }
    }

    companion object {
        const val DEFAULT_INTERVAL = 5
    }
}
