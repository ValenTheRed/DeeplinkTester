package com.example.deeplinktester.ui

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deeplinktester.data.DataStoreInstance.DEEPLINKS_LIST
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

typealias Deeplinks = LinkedHashSet<String>

class HomeModel(
    dataStore: DataStore<Preferences>,
    initialDeeplinks: Deeplinks = linkedSetOf(),
) : ViewModel() {
    private val _deeplinks = MutableStateFlow(initialDeeplinks)
    private val _dataStore = dataStore
    val deeplinks: StateFlow<Deeplinks>
        get() = _deeplinks.asStateFlow()

    init {
        viewModelScope.launch {
            _dataStore.data.collect { store ->
                store[DEEPLINKS_LIST]?.let { history ->
                    _deeplinks.update { state ->
                        Json.decodeFromString<Deeplinks>(history)
                    }
                }
            }
        }
    }

    fun push(deeplink: String) {
        _deeplinks.update { state ->
            val deeplinks = Deeplinks(state)
            deeplinks.add(deeplink)
            deeplinks
        }
        saveHistory()
    }

    fun delete(deeplink: String) {
        _deeplinks.update { state ->
            val deeplinks = Deeplinks(state)
            deeplinks.remove(deeplink)
            deeplinks
        }
        saveHistory()
    }

    fun saveHistory() {
        viewModelScope.launch {
            _dataStore.edit { store ->
                store[DEEPLINKS_LIST] = Json.encodeToString(_deeplinks.value)
            }
        }
    }
}
