package com.example.deeplinktester.ui

import kotlinx.serialization.Serializable

@Serializable
data class AppUiState(
    val list: List<String> = emptyList(),
    val input: String = "",
)