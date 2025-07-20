package com.example.deeplinktester.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HistoryList(
    data: List<String>,
    onDelete: (Int) -> Unit,
    showSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item(key = "history_heading") {
            Text(
                text = "History",
                style = MaterialTheme.typography.titleLarge
            )
        }
        itemsIndexed(data) { index, d ->
            HistoryItem(
                deeplink = d,
                onDelete = { onDelete(index) },
                showSnackbar
            )
        }
    }
}
