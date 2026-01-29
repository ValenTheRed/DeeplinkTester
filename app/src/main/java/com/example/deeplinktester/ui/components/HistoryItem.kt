package com.example.deeplinktester.ui.components

import android.content.Intent
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.core.net.toUri
import com.example.deeplinktester.R
import com.example.deeplinktester.ui.screens.ActiveSnackbarController
import com.example.deeplinktester.ui.theme.Density
import com.example.deeplinktester.utils.onlyApplyIf

@Composable
fun HistoryItem(
    deeplink: String,
    modifier: Modifier = Modifier,
    highlight: String? = null,
    onDelete: (() -> Unit)? = null,
) {
    val clipboardManager = LocalClipboardManager.current
    val ctx = LocalContext.current
    val snackbar = ActiveSnackbarController.current

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
            .background(MaterialTheme.colorScheme.primaryContainer),
    ) {
        val text = if (highlight?.isNotEmpty() == true) {
            buildAnnotatedString {
                val parts = deeplink.split(
                    Regex(highlight, RegexOption.IGNORE_CASE),
                    2
                )
                if (parts.size != 2) {
                    append(deeplink)
                } else {
                    append(parts[0])
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textDecoration = TextDecoration.Underline,
                        )
                    ) {
                        append(highlight)
                    }
                    append(parts[1])
                }
                toAnnotatedString()
            }
        } else {
            AnnotatedString(deeplink)
        }
        Text(
            text = text,
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
                // Only show a toast for Android 12 and lower.
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                    snackbar.show(ctx.resources.getString(R.string.deeplink_copied))
                }
                clipboardManager.setText(AnnotatedString(deeplink))
            },
            modifier = Modifier
                .size(Density.IconSize)
                .onlyApplyIf(onDelete != null),
        ) {
            Icon(
                painter = painterResource(R.drawable.content_copy),
                contentDescription = stringResource(R.string.copy_deeplink),
            )
        }
        if (onDelete != null) {
            IconButton(
                onClick = onDelete,
            ) {
                Icon(
                    painter = painterResource(R.drawable.delete),
                    contentDescription = stringResource(R.string.delete_deeplink),
                )
            }
        }
    }
}
