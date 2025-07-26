package com.example.deeplinktester.ui

import kotlinx.serialization.Serializable

@Serializable
data class HistoryUiState(
    val list: List<String> = emptyList(),
)