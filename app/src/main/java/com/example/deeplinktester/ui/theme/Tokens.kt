package com.example.deeplinktester.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object Density {
    /**
     * These mostly matche the values used by ShapeDefaults (ShapeTokens)
     * tokens.
     */
    val ExtraExtraSmall = 2.dp
    val ExtraSmall = 4.dp
    val Small = 8.dp
    val Medium = 12.dp
    val Large = 16.dp
    val ExtraLarge = 28.dp

    val IconSize = 40.dp
}

fun Modifier.appEdgePadding(): Modifier {
    return this.padding(horizontal = Density.Medium)
}
