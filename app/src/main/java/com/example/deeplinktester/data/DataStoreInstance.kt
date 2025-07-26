package com.example.deeplinktester.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object DataStoreInstance {
    val Context.dataStore: DataStore<Preferences> by
        preferencesDataStore(name = "store")

    val HISTORY_LIST = stringPreferencesKey("history")
}