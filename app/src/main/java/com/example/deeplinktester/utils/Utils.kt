package com.example.deeplinktester.utils

import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** A immediate/leading debounce implementation. */
fun debounce(
    skipMs: Long = 400L,
    coroutineScope: CoroutineScope,
    fn: () -> Unit
): () -> Unit {
    var throttleJob: Job? = null
    return {
        if (throttleJob?.isCompleted != false) {
            throttleJob = coroutineScope.launch {
                fn()
                delay(skipMs)
            }
        }
    }
}

fun Modifier.onlyApplyIf(
    condition: Boolean,
): Modifier {
    return if (condition) this else Modifier
}
