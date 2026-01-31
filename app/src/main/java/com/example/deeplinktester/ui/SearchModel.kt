package com.example.deeplinktester.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
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

typealias SearchQueries = LinkedHashSet<String>

sealed class SearchResults {
    data class Queries(val queries: SearchQueries) : SearchResults()
    data class Links(val links: Deeplinks) : SearchResults()
    data object Empty : SearchResults()
}

class SearchModel(
    dataStore: DataStore<Preferences>,
    deeplinks: StateFlow<Deeplinks>,
    initialSearchHistory: SearchQueries = linkedSetOf(),
) : ViewModel() {
    private val _dataStore = dataStore
    private val _searchHistory = MutableStateFlow(initialSearchHistory)
    val query = TextFieldState()

    val searchResults: StateFlow<SearchResults> = combine(
        snapshotFlow { query.text },
        deeplinks,
        _searchHistory
    ) { q, links, history ->
        if (q.isBlank()) {
            SearchResults.Queries(history)
        } else {
            SearchResults.Links(links.filterTo(LinkedHashSet()) {
                it.contains(q, ignoreCase = true)
            })
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        SearchResults.Empty
    )

    fun highlightDeeplink(deeplink: String, style: SpanStyle): AnnotatedString {
        return buildAnnotatedString {
            val startIndex = deeplink.indexOf(query.text.toString(), ignoreCase = true)
            if (startIndex == -1) {
                append(deeplink)
            } else {
                append(deeplink.substring(0 until startIndex))
                withStyle(style) {
                    append(
                        deeplink.substring(
                            startIndex until startIndex + query.text.length
                        )
                    )
                }
                append(
                    deeplink.substring(
                        startIndex + query.text.length
                    )
                )
            }
            toAnnotatedString()
        }
    }

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
        query.setTextAndPlaceCursorAtEnd(value)
    }

    fun push(value: String? = null, index: Int = 0) {
        _searchHistory.update { state ->
            val query = value ?: query.text.toString()
            if (query in state) {
                state
            }
            val list = state.toMutableList()
            list.add(index, query)
            LinkedHashSet(list)
        }
        saveHistory()
    }

    fun delete(query: String) {
        _searchHistory.update { state ->
            val queries = SearchQueries(state)
            queries.remove(query)
            queries
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
