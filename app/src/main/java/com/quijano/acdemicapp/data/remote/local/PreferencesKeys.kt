package com.quijano.acdemicapp.data.remote.local


import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val TOKEN = stringPreferencesKey("jwt_token")
    val USER_ID = stringPreferencesKey("user_id")
    val ROLE = stringPreferencesKey("user_role")
}