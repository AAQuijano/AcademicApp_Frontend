package com.quijano.acdemicapp.data.remote.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para crear el DataStore (debe ser global)
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

class SessionManager(private val context: Context) {

    companion object {
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("key_access_token")
        private val KEY_USER_ID = stringPreferencesKey("key_user_id")
        private val KEY_USER_ROLE = stringPreferencesKey("key_user_role")
        private val KEY_USERNAME = stringPreferencesKey("key_username")
    }

    suspend fun saveUserSession(
        token: String,
        userId: String,
        role: String,
        username: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[KEY_ACCESS_TOKEN] = token
            preferences[KEY_USER_ID] = userId
            preferences[KEY_USER_ROLE] = role
            preferences[KEY_USERNAME] = username
        }
    }

    suspend fun clearUserSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    val accessToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[KEY_ACCESS_TOKEN]
        }

    val userId: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[KEY_USER_ID]
        }

    val userRole: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[KEY_USER_ROLE]
        }

    val username: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[KEY_USERNAME]
        }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            !preferences[KEY_ACCESS_TOKEN].isNullOrEmpty()
        }


    fun decodeRoleFromToken(token: String): String? {
        val payload = token.split(".").getOrNull(1) ?: return null
        val decodedBytes = android.util.Base64.decode(payload, android.util.Base64.URL_SAFE)
        val json = String(decodedBytes, Charsets.UTF_8)

        // Usar expresión regular para extraer "role":"..."
        val regex = Regex("\"role\"\\s*:\\s*\"(.*?)\"")
        return regex.find(json)?.groupValues?.get(1)
    }

    suspend fun saveSession(token: String, userId: String, role: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ACCESS_TOKEN] = token
            prefs[KEY_USER_ID] = userId
            prefs[KEY_USER_ROLE] = role
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }

}

