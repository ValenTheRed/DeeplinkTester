package com.example.deeplinktester.ui.components

import android.content.Intent
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.deeplinktester.ui.theme.Shapes
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
    val minimumInteractiveComponentSize = LocalMinimumInteractiveComponentSize
        .current
    val shape = Shapes.ListItem

    Column(
        verticalArrangement = Arrangement.spacedBy(Density.ExtraExtraSmall),
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .heightIn(minimumInteractiveComponentSize)
                .clip(shape)
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
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
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
                    .fillMaxWidth()
                    .padding(
                        start = Density.Large,
                        end = Density.Small,
                        top = Density.Small,
                        bottom = Density.Small,
                    ),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .clip(shape)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            val buttonModifier = Modifier
                .heightIn(minimumInteractiveComponentSize)
                .clip(shape)
            val showDeleteButton = onDelete != null

            if (showDeleteButton) {
                TextButton(
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
                    shape = shape,
                    modifier = buttonModifier
                        .weight(1f)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.delete),
                        contentDescription = stringResource(R.string.delete_deeplink),
                        modifier = Modifier.size(Density.TextIconSize)
                    )
                    Spacer(modifier = Modifier.size(Density.ExtraSmall))
                    Text(text = "Delete")
                }
                VerticalDivider(
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .height(minimumInteractiveComponentSize - Density.Medium)
                )
            }
            if (canOverflow) {
                TextButton(
                    onClick = {
                        expanded = !expanded
                    },
                    shape = shape,
                    modifier = buttonModifier
                        .weight(if (showDeleteButton) 1.25f else 1f)
                ) {
                    if (expanded) {
                        Icon(
                            painter = painterResource(R.drawable.collapse_content),
                            contentDescription = stringResource(R.string.collapse_content),
                            modifier = Modifier.size(Density.TextIconSize),
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.expand_content),
                            contentDescription = stringResource(R.string.expand_content),
                            modifier = Modifier.size(Density.TextIconSize),
                        )
                    }
                    Spacer(modifier = Modifier.size(Density.ExtraSmall))
                    Text(text = if (expanded) "Read Less" else "Read More")
                }
                VerticalDivider(
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .height(minimumInteractiveComponentSize - Density.Medium)
                )
            }
            TextButton(
                onClick = {
                    // Only show a toast for Android 12 and lower.
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                        snackbar.show(resources.getString(R.string.deeplink_copied))
                    }
                    clipboardManager.setText(AnnotatedString(deeplink))
                },
                shape = shape,
                modifier = buttonModifier
                    .weight(1f)
            ) {
                Icon(
                    painter = painterResource(R.drawable.content_copy),
                    contentDescription = stringResource(R.string.copy_deeplink),
                    modifier = Modifier.size(Density.TextIconSize),
                )
                Spacer(modifier = Modifier.size(Density.ExtraSmall))
                Text(text = "Copy")
            }
        }
    }
}
