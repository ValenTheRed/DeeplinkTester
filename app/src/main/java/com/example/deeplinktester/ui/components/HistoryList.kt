package com.example.deeplinktester.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import com.example.deeplinktester.ui.theme.Density
import com.example.deeplinktester.ui.theme.Shapes
import com.example.deeplinktester.ui.theme.appEdgePadding

@Composable
fun HistoryList(
    data: Collection<String>,
    dividerThickness: Dp,
    modifier: Modifier = Modifier,
    itemContent: @Composable LazyItemScope.(
        data: String,
        index: Int,
        modifier: Modifier,
    ) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .appEdgePadding()
            .clip(MaterialTheme.shapes.large),
    ) {
        itemsIndexed(
            items = data.toList(),
            key = { _, v -> v }
        ) { index, d ->
            itemContent(
                d,
                index,
                Modifier
                    .animateItem()
                    .clip(
                        if (data.size == 1) {
                            Shapes.OnlyListItem
                        } else if (index == 0) {
                            Shapes.FirstListItem
                        } else if (index == data.size - 1) {
                            Shapes.LastListItem
                        } else {
                            Shapes.ListItem
                        }
                    )
            )
            if (index < data.size - 1) {
                HorizontalDivider(
                    thickness = dividerThickness, //Density.ExtraSmall,
                    color = MaterialTheme.colorScheme.background,
                )
            }
        }
    }
}
