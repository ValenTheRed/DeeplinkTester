package com.example.deeplinktester.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.deeplinktester.R
import com.example.deeplinktester.ui.theme.Density
import com.example.deeplinktester.ui.theme.Shapes
import com.example.deeplinktester.ui.theme.appEdgePadding

@Composable
fun HistoryList(
    data: List<String>,
    onDelete: (Int) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .appEdgePadding()
            .clip(MaterialTheme.shapes.large),
    ) {
        item(key = "history_heading") {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.history_title),
                    style = MaterialTheme.typography.titleLarge,
                )
                IconButton(
                    onClick = onSearch,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.search),
                        contentDescription = stringResource(R.string.search_deeplinks),
                    )
                }
            }
        }
        itemsIndexed(data) { index, d ->
            HistoryItem(
                deeplink = d,
                onDelete = { onDelete(index) },
                modifier = Modifier
                    .animateItem()
                    .clip(
                        when (index) {
                            0 -> Shapes.FirstListItem
                            data.size - 1 -> Shapes.LastListItem
                            else -> MaterialTheme.shapes.extraSmall
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
