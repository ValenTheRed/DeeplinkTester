package com.example.deeplinktester.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HistoryViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState>
        get() = _uiState.asStateFlow()

    private fun resetHistory() {
        _uiState.value = HistoryUiState()
    }

    fun push(deeplink: String) {
        _uiState.update { state ->
            val updatedList = state.list.toMutableList()
            updatedList.add(deeplink)
            state.copy(updatedList)
        }
    }

    init {
        resetHistory()
    }
}