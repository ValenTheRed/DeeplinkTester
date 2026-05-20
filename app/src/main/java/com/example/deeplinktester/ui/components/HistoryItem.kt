package com.example.deeplinktester.ui.components

import android.content.Intent
import android.os.Build
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.net.toUri
import com.example.deeplinktester.R
import com.example.deeplinktester.ui.screens.LocalActiveSnackbarController
import com.example.deeplinktester.ui.theme.Density
import com.example.deeplinktester.ui.theme.MAX_TEXT_LINES
import kotlin.Int

@Composable
fun HistoryItem(
    deeplink: String,
    modifier: Modifier = Modifier,
    highlightedDeeplink: AnnotatedString? = null,
    onDelete: (() -> Unit)? = null,
    onUndo: (() -> Unit)? = null,
) {
    val clipboardManager = LocalClipboardManager.current
    val ctx = LocalContext.current
    val resources = LocalResources.current
    val snackbar = LocalActiveSnackbarController.current
    var expanded by remember { mutableStateOf(false) }
    var canOverflow by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .clickable(
                enabled = true,
                onClick = {
                    ctx.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            deeplink.toUri()
                        )
                    )
                }
            )
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        Text(
            maxLines = if (expanded) Int.MAX_VALUE else MAX_TEXT_LINES,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { result ->
                if (result.didOverflowHeight) {
                    canOverflow = true
                }
            },
            text = highlightedDeeplink ?: AnnotatedString(deeplink),
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
        Row(
            modifier = Modifier.align(Alignment.Top)
        ) {
            if (canOverflow) {
                IconButton(
                    onClick = {
                        expanded = !expanded
                    },
                ) {
                    if (expanded) {
                        Icon(
                            painter = painterResource(R.drawable.collapse_content),
                            contentDescription = stringResource(R.string.collapse_content),
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.expand_content),
                            contentDescription = stringResource(R.string.expand_content),
                        )
                    }
                }
            }
            IconButton(
                onClick = {
                    // Only show a toast for Android 12 and lower.
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                        snackbar.show(resources.getString(R.string.deeplink_copied))
                    }
                    clipboardManager.setText(AnnotatedString(deeplink))
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.content_copy),
                    contentDescription = stringResource(R.string.copy_deeplink),
                )
            }
            if (onDelete != null) {
                IconButton(
                    onClick = {
                        onDelete()
                        if (onUndo == null) {
                            snackbar.show(
                                resources.getString(R.string.deeplink_deleted),
                            )
                        } else {
                            snackbar.show(
                                resources.getString(R.string.deeplink_deleted),
                                resources.getString(R.string.undo),
                                { r ->
                                    when (r) {
                                        SnackbarResult.Dismissed -> return@show
                                        SnackbarResult.ActionPerformed -> onUndo()
                                    }
                                }
                            )
                        }
                    },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.delete),
                        contentDescription = stringResource(R.string.delete_deeplink),
                    )
                }
            }
        }
    }
}
