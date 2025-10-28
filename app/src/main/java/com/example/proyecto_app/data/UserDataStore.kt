package com.example.proyecto_app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

object UserDataKeys {
    val USER_EMAIL = stringPreferencesKey("user_email")
    val USER_PASSWORD = stringPreferencesKey("user_password")
    val USER_LOCATION = stringPreferencesKey("user_location")
}
