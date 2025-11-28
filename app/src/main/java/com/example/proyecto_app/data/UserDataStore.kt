package com.example.proyecto_app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.proyecto_app.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// Extension property to get DataStore instance
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

object UserDataKeys {
    val USER_DATA = stringPreferencesKey("user_data")
    val USER_PASSWORD = stringPreferencesKey("user_password") // Storing password like this is insecure, for academic purposes only
}

// Extension function to save user data
suspend fun Context.saveUserData(user: User, password: String) {
    dataStore.edit {
        val userJson = Json.encodeToString(user)
        it[UserDataKeys.USER_DATA] = userJson
        it[UserDataKeys.USER_PASSWORD] = password // Storing password
    }
}

// Extension function to get user data
fun Context.getUserData(): Flow<User?> {
    return dataStore.data.map {
        val userJson = it[UserDataKeys.USER_DATA]
        if (userJson != null) {
            Json.decodeFromString<User>(userJson)
        } else {
            null
        }
    }
}

// Extension function to get user by email (simplified)
fun Context.getUserByEmail(email: String): Flow<User?> {
    return dataStore.data.map {
        val userJson = it[UserDataKeys.USER_DATA]
        if (userJson != null) {
            val user = Json.decodeFromString<User>(userJson)
            if (user.email.equals(email, ignoreCase = true)) {
                user
            } else {
                null
            }
        } else {
            null
        }
    }
}

// Extension function to get stored password
fun Context.getUserPassword(): Flow<String?> {
    return dataStore.data.map { it[UserDataKeys.USER_PASSWORD] }
}

// Helper for LoginViewModel
object AppPreferences {
    suspend fun getPasswordForUser(context: Context, email: String): String? {
        val user = context.getUserByEmail(email).first()
        return if (user != null) {
            context.dataStore.data.first()[UserDataKeys.USER_PASSWORD]
        } else {
            null
        }
    }
}
