package com.example.deeplinktester.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Density mostly matches the values used by ShapeDefaults (ShapeTokens) tokens.
 */
object Density {
    val ExtraExtraSmall = 2.dp
    val ExtraSmall = 4.dp
    val Small = 8.dp
    val Medium = 12.dp
    val Large = 16.dp
    val ExtraLarge = 28.dp
}

fun Modifier.appEdgePadding(): Modifier {
    return this.padding(horizontal = 10.dp)
}
