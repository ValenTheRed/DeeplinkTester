package com.example.deeplinktester.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.deeplinktester.R
import com.example.deeplinktester.ui.screens.LocalActiveSnackbarController
import com.example.deeplinktester.ui.theme.Density

@Composable
fun SearchResultQuery(
    query: String,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onUndo: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val resources = LocalResources.current
    val snackbar = LocalActiveSnackbarController.current

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
        Box(
            contentAlignment = Alignment.Center,
            // NOTE: Occupy the same space as `IconButton` i.e. 40.dp Box
            //  with 4.dp padding for a total of 48.dp.
            modifier = Modifier
                .padding(Density.ExtraSmall)
                .size(Density.IconSize)
        ) {
            Icon(
                painter = painterResource(R.drawable.recent_search),
                contentDescription = stringResource(R.string.recent_search_query),
            )
        }
        Text(
            text = query,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = Density.Small),
            style = MaterialTheme.typography.bodyMedium,
        )
        IconButton(
            onClick = {
                onDelete()
                snackbar.show(
                    resources.getString(R.string.search_query_deleted),
                    resources.getString(R.string.undo),
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
