package com.capstone.miemo.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.capstone.miemo.data.local.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name="session")

class AppPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun isLoggedIn(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            // IF token empty, user didn't log in
            preferences[AUTH_TOKEN_KEY]?.isNotEmpty() ?: false
        }
    }

    fun getAuthToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[AUTH_TOKEN_KEY] ?: ""
        }
    }

    fun getSession(): Flow<User>{
        return dataStore.data.map { preferences ->
            User(
                preferences[AUTH_USERID_KEY] ?: "",
                preferences[AUTH_USERNAME_KEY] ?:"",
                preferences[AUTH_TOKEN_KEY] ?: ""
            )
        }
    }

    suspend fun saveAuthUser(user: User) {
        dataStore.edit { preferences ->
            preferences[AUTH_USERID_KEY] = user.userId
            preferences[AUTH_USERNAME_KEY] = user.name
            preferences[AUTH_TOKEN_KEY] = user.token
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppPreferences? = null

        private val AUTH_USERID_KEY = stringPreferencesKey("auth_userid")
        private val AUTH_USERNAME_KEY = stringPreferencesKey("auth_username")
        private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")

        fun getInstance(dataStore: DataStore<Preferences>): AppPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = AppPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}