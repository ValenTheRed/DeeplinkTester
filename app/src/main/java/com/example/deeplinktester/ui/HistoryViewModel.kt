package com.example.deeplinktester.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HistoryViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState>
        get() = _uiState.asStateFlow()

    private fun resetHistory() {
        _uiState.value = HistoryUiState()
    }

    init {
        resetHistory()
    }
}