package com.example.deeplinktester.ui

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deeplinktester.data.DataStoreInstance.HISTORY_LIST
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class HistoryViewModel(
    dataStore: DataStore<Preferences>,
    initialUiState: HistoryUiState = HistoryUiState(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(initialUiState)
    val uiState: StateFlow<HistoryUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                dataStore.data.map { store ->
                    store[HISTORY_LIST]?.let {
                        Json.decodeFromString<HistoryUiState>(it)
                    }
                }.single() ?: initialUiState
            }
        }
    }

    fun push(deeplink: String) {
        _uiState.update { state ->
            val updatedList = state.list.toMutableList()
            updatedList.add(deeplink)
            state.copy(updatedList.toList())
        }
    }

    fun delete(index: Int) {
        _uiState.update { state ->
            val updatedList = state.list.toMutableList()
            updatedList.removeAt(index)
            state.copy(updatedList.toList())
        }
    }
}