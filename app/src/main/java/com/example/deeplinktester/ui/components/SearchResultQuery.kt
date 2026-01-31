package com.example.deeplinktester.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.deeplinktester.R
import com.example.deeplinktester.ui.screens.ActiveSnackbarController
import com.example.deeplinktester.ui.theme.Density

@Composable
fun SearchResultQuery(
    query: String,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onUndo: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val ctx = LocalContext.current
    val snackbar = ActiveSnackbarController.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .clickable(
                enabled = true,
                onClick = onClick
            )
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        Text(
            text = query,
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = Density.Large,
                    end = Density.Small,
                    top = Density.Small,
                    bottom = Density.Small,
                ),
            style = MaterialTheme.typography.bodyMedium,
        )
        IconButton(
            onClick = {
                onDelete()
                snackbar.show(
                    ctx.resources.getString(R.string.search_query_deleted),
                    ctx.resources.getString(R.string.undo),
                    { r ->
                        when (r) {
                            SnackbarResult.Dismissed -> return@show
                            SnackbarResult.ActionPerformed -> onUndo()
                        }
                    }
                )
            },
        ) {
            Icon(
                painter = painterResource(R.drawable.delete),
                contentDescription = stringResource(R.string.delete_deeplink),
            )
        }
    }
}
