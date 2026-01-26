package com.example.deeplinktester.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.deeplinktester.ui.theme.Density
import com.example.deeplinktester.ui.theme.Shapes
import com.example.deeplinktester.ui.theme.appEdgePadding

@Composable
fun HistoryList(
    data: List<String>,
    modifier: Modifier = Modifier,
    onDelete: ((Int) -> Unit)? = null,
) {
    LazyColumn(
        modifier = modifier
            .appEdgePadding()
            .clip(MaterialTheme.shapes.large),
    ) {
        itemsIndexed(data) { index, d ->
            HistoryItem(
                deeplink = d,
                onDelete = onDelete?.let { { it(index) } },
                modifier = Modifier
                    .animateItem()
                    .clip(
                        if (data.size == 1) {
                            Shapes.OnlyListItem
                        } else if (index == 0) {
                            Shapes.FirstListItem
                        } else if (index == data.size - 1) {
                            Shapes.LastListItem
                        } else {
                            MaterialTheme.shapes.extraSmall
                        }
                    )
            )
            if (index < data.size - 1) {
                HorizontalDivider(
                    thickness = Density.ExtraExtraSmall,
                    color = MaterialTheme.colorScheme.background,
                )
            }
        }
    }
}
