package com.nxim.pathfind.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "pathfind_auth")

class AuthRepository(private val context: Context) {
    private val serverUrlKey = stringPreferencesKey("pathfind_server_url")
    private val apiTokenKey = stringPreferencesKey("pathfind_api_token")

    val authState: Flow<AuthState> = context.dataStore.data.map { prefs ->
        val serverUrl = prefs[serverUrlKey] ?: ""
        val apiToken = prefs[apiTokenKey] ?: ""
        AuthState(serverUrl, apiToken)
    }

    suspend fun saveCredentials(serverUrl: String, apiToken: String) {
        context.dataStore.edit { prefs ->
            prefs[serverUrlKey] = serverUrl
            prefs[apiTokenKey] = apiToken
        }
    }

    suspend fun clearCredentials() {
        context.dataStore.edit { prefs ->
            prefs.remove(serverUrlKey)
            prefs.remove(apiTokenKey)
        }
    }
}

data class AuthState(
    val serverUrl: String,
    val apiToken: String
) {
    val isAuthenticated: Boolean
        get() = serverUrl.isNotEmpty() && apiToken.isNotEmpty()
}
