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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

typealias SearchQueries = List<String>

sealed class SearchResults {
    data class Queries(val queries: SearchQueries) : SearchResults()
    data class Links(val links: Deeplinks) : SearchResults()
    data object Empty : SearchResults()
}

class SearchModel(
    dataStore: DataStore<Preferences>,
    deeplinks: StateFlow<Deeplinks>,
    initialSearchHistory: SearchQueries = emptyList(),
) : ViewModel() {
    private val _dataStore = dataStore
    private val _searchHistory = MutableStateFlow(initialSearchHistory)
    var query by mutableStateOf("")
        private set

    val searchResults: StateFlow<SearchResults> = combine(
        snapshotFlow { query },
        deeplinks,
        _searchHistory
    ) { q, links, history ->
        if (q.isBlank()) {
            SearchResults.Queries(history)
        } else {
            SearchResults.Links(links.filter {
                it.contains(q, ignoreCase = true)
            })
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        SearchResults.Empty
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

    fun push(query: String) {
        _searchHistory.update { state ->
            val updatedList = state.toMutableList()
            updatedList.add(query)
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
