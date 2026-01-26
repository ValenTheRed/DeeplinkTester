package com.example.deeplinktester.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape

object Shapes {
    val FirstListItem =
        RoundedCornerShape(
            topEnd = Density.Large,
            topStart = Density.Large,
            bottomEnd = Density.ExtraSmall,
            bottomStart = Density.ExtraSmall,
        )

    val LastListItem =
        RoundedCornerShape(
            topEnd = Density.ExtraSmall,
            topStart = Density.ExtraSmall,
            bottomEnd = Density.Large,
            bottomStart = Density.Large,
        )
}
