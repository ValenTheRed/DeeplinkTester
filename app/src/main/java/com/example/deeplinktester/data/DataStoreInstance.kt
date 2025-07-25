package com.example.deeplinktester.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.deeplinktester.ui.HistoryUiState
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.let

object DataStoreInstance {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "store"
    )

    val HISTORY_LIST = stringPreferencesKey("history")

    suspend fun saveHistory(ctx: Context, uiState: HistoryUiState) {
        ctx.dataStore.edit { store ->
            store[HISTORY_LIST] = Json.encodeToString(uiState)
        }
    }

    suspend fun fetchHistory(ctx: Context): HistoryUiState? {
        return ctx.dataStore.data.map { store ->
            store[HISTORY_LIST]?.let {
                Json.decodeFromString<HistoryUiState>(it)
            }
        }.single()
    }
}