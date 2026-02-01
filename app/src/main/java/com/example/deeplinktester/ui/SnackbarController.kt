package com.example.deeplinktester.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SnackbarController(
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
) {
    private val hostState = snackbarHostState
    private val scope = coroutineScope

    fun show(
        message: String,
        actionLabel: String? = null,
        onResult: ((r: SnackbarResult) -> Unit)? = null
    ) {
        scope.launch {
            hostState.currentSnackbarData?.dismiss()
            val result = hostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                withDismissAction = true
            )
            onResult?.invoke(result)
        }
    }
}