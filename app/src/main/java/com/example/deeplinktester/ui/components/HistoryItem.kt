package com.example.deeplinktester.ui.components

import android.content.Intent
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
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
import androidx.core.net.toUri
import com.example.deeplinktester.ActiveSnackbarController
import com.example.deeplinktester.R

@Composable
fun HistoryItem(
    deeplink: String,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current
    val ctx = LocalContext.current
    val snackbar = ActiveSnackbarController.current

    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable(
            enabled = true,
            onClick = {
                ctx.startActivity(Intent(Intent.ACTION_VIEW, deeplink.toUri()))
            }
        ),
    ) {
        Text(
            text = deeplink,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )
        IconButton (
            onClick = {
                // Only show a toast for Android 12 and lower.
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                    snackbar.show(ctx.resources.getString(R.string.deeplink_copied))
                }
                clipboardManager.setText(AnnotatedString(deeplink))
            },
        ) {
            Icon(
                painter = painterResource(R.drawable.content_copy),
                contentDescription = stringResource(R.string.copy_deeplink),
            )
        }
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
