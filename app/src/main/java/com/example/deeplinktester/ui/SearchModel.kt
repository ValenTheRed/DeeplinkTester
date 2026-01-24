package com.example.deeplinktester.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deeplinktester.data.DataStoreInstance.SEARCH_HISTORY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

typealias SearchHistory = List<String>

class SearchModel(
    dataStore: DataStore<Preferences>,
    deeplinks: StateFlow<Deeplinks>,
    initialSearchHistory: SearchHistory = emptyList<String>(),
) : ViewModel() {
    private val _dataStore = dataStore
    private val _searchHistory = MutableStateFlow(initialSearchHistory)
    val searchHistory: StateFlow<SearchHistory>
        get() = _searchHistory.asStateFlow()
    var query by mutableStateOf("")
        private set

    val searchResults: StateFlow<Deeplinks> = snapshotFlow { query }
        .combine(deeplinks) { q, links ->
            if (q.isBlank()) links
            else links.filter { it.contains(q, ignoreCase = true) }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    init {
        viewModelScope.launch {
            _dataStore.data.collect { store ->
                store[SEARCH_HISTORY]?.let { history ->
                    _searchHistory.update { state ->
                        Json.decodeFromString<Deeplinks>(history)
                    }
                }
            }
        }
    }

    fun onSearch(value: String) {
        query = value
    }

    fun push(deeplink: String) {
        _searchHistory.update { state ->
            val updatedList = state.toMutableList()
            updatedList.add(deeplink)
            updatedList
        }
        saveHistory()
    }

    fun delete(index: Int) {
        _searchHistory.update { state ->
            val updatedList = state.toMutableList()
            updatedList.removeAt(index)
            updatedList
        }
        saveHistory()
    }

    fun saveHistory() {
        viewModelScope.launch {
            _dataStore.edit { store ->
                store[SEARCH_HISTORY] =
                    Json.encodeToString(_searchHistory.value)
            }
        }
    }
}
