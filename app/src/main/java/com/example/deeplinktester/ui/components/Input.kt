package com.example.deeplinktester.ui.components

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.deeplinktester.ActiveSnackbarController
import com.example.deeplinktester.R
import com.example.deeplinktester.utils.debounce

@Composable
fun Input(
    onOpenDeeplink: (deeplink: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val ctx = LocalContext.current
    val snackbar = ActiveSnackbarController.current
    var value by rememberSaveable { mutableStateOf("https://google.com") }

    Column(
        modifier = modifier,
    ) {
        TextField(
            value = value,
            onValueChange = {
                value = it
            },
            placeholder = { Text(
                text = stringResource(R.string.input_placeholder))
            },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(
                    onClick = {
                        value = ""
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(
                            R.string.clear_input
                        ),
                    )
                }
            }
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = debounce(400, {
                if (value.trim() == "") {
                    return@debounce
                }
                try {
                    ctx.startActivity(Intent(Intent.ACTION_VIEW, value.toUri()))
                    onOpenDeeplink(value)
                } catch (_: ActivityNotFoundException) {
                    snackbar.show(
                        ctx.resources.getString(
                            R.string.activity_not_found_exception_msg
                        )
                    )
                }
            }),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.open_deeplink))
        }
    }
}

