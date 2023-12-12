package com.capstone.miemo.ui

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.miemo.data.AppPreferences
import com.capstone.miemo.di.Injection
import com.capstone.miemo.ui.auth.AuthViewModel


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            val pref = AppPreferences.getInstance(context.dataStore)
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(pref, Injection.provideAuthRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}