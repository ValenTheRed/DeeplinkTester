package com.example.deeplinktester.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deeplinktester.data.DataStoreInstance.HISTORY_LIST
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AppViewModel(
    dataStore: DataStore<Preferences>,
    initialUiState: HistoryUiState = HistoryUiState(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(initialUiState)
    private val _dataStore = dataStore
    val uiState: StateFlow<HistoryUiState>
        get() = _uiState.asStateFlow()
    var inputValue by mutableStateOf("https://google.com")
        private set

    init {
        viewModelScope.launch {
            _dataStore.data.collect { store ->
                store[HISTORY_LIST]?.let { history ->
                    _uiState.update { state ->
                        Json.decodeFromString<HistoryUiState>(history)
                    }
                }
            }
        }
    }

    fun updateInputValue(value: String) {
        inputValue = value
    }

    fun push(deeplink: String) {
        _uiState.update { state ->
            val updatedList = state.list.toMutableList()
            updatedList.add(deeplink)
            state.copy(list = updatedList.toList())
        }
        saveHistory()
    }

    fun delete(index: Int) {
        _uiState.update { state ->
            val updatedList = state.list.toMutableList()
            updatedList.removeAt(index)
            state.copy(list = updatedList.toList())
        }
        saveHistory()
    }

    fun saveHistory() {
        viewModelScope.launch {
            _dataStore.edit { store ->
                store[HISTORY_LIST] = Json.encodeToString(_uiState.value)
            }
        }
    }
}