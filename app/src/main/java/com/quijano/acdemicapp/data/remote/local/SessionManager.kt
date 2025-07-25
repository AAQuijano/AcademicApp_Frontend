package com.quijano.acdemicapp.data.remote.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

class SessionManager(private val context: Context) {

    companion object {
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("key_access_token")
        private val KEY_USER_ID = stringPreferencesKey("key_user_id")
        private val KEY_USER_ROLE = stringPreferencesKey("key_user_role")
        private val KEY_USERNAME = stringPreferencesKey("key_username")
    }

    suspend fun saveUserSession(token: String, userId: String, role: String, username: String) {
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

    // Alias para compatibilidad con AuthViewModel
    suspend fun saveSession(token: String, userId: String, role: String) {
        saveUserSession(token = token, userId = userId, role = role, username = "")
    }

    suspend fun clearSession() {
        clearUserSession()
    }

    val accessToken: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[KEY_ACCESS_TOKEN] }

    val userId: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[KEY_USER_ID] }

    val userRole: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[KEY_USER_ROLE] }

    val username: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[KEY_USERNAME] }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences -> !preferences[KEY_ACCESS_TOKEN].isNullOrEmpty() }
}