package com.example.aldrovandishunt.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class SettingsRepository (private val context: Context){
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_preferences")
        private val INTRO = booleanPreferencesKey("intro")
    }

    suspend fun setIntro(value: Boolean){
        context.dataStore.edit { settings ->
            settings[INTRO] = value
        }
    }

    suspend fun getIntro(): Boolean{
        val settings = context.dataStore.data.first()
        return settings[INTRO] ?: true
    }



}