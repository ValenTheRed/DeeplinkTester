package com.example.deeplinktester.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

/** A immediate/leading debounce implementation. */
@Composable
fun debounce(
    intervalMillis: Long,
    onClick: () -> Unit,
): () -> Unit {
    var lastTimeMillis: Long by remember { mutableLongStateOf(0L) }
    return {
        val currentMillis = System.currentTimeMillis()
        if ((currentMillis - lastTimeMillis) >= intervalMillis) {
            onClick()
        }
        lastTimeMillis = currentMillis
    }
}

fun Modifier.onlyApplyIf(
    condition: Boolean,
): Modifier {
    return if (condition) this else Modifier
}
