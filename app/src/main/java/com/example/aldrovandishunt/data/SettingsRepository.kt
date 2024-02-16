package com.example.aldrovandishunt.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class SettingsRepository (private val context: Context){
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_preferences")
        private val INTRO = booleanPreferencesKey("intro")
        private val HINT_COINS = intPreferencesKey("hint_coins")
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

    suspend fun setHintCoins(value: Int){
        context.dataStore.edit { settings ->
            settings[HINT_COINS] = value
        }
    }

    suspend fun getHintCoins(): Int{
        val settings = context.dataStore.data.first()
        return settings[HINT_COINS] ?: 3
    }




}