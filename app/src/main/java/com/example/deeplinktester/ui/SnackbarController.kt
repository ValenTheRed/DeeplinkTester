package com.example.deeplinktester.ui

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SnackbarController(
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    private val hostState = snackbarHostState
    private val scope = coroutineScope

    fun show(message: String) {
        scope.launch {
            hostState.showSnackbar(
                message = message,
                withDismissAction = true,
            )
        }
    }
}