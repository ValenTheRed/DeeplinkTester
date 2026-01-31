package com.example.deeplinktester.ui.components

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.core.net.toUri
import com.example.deeplinktester.R
import com.example.deeplinktester.ui.screens.ActiveSnackbarController
import com.example.deeplinktester.ui.theme.Density
import com.example.deeplinktester.utils.debounce

@Composable
fun Input(
    onOpenDeeplink: (deeplink: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val ctx = LocalContext.current
    val snackbar = ActiveSnackbarController.current
    val scope = rememberCoroutineScope()

    var value by rememberSaveable { mutableStateOf("") }
    val onOpen = debounce(coroutineScope = scope) {
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
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                value = it
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.input_placeholder)
                )
            },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Go
            ),
            keyboardActions = KeyboardActions(
                onGo = { onOpen() }
            ),
            trailingIcon = {
                if (value.isEmpty()) {
                    return@OutlinedTextField
                }
                    IconButton(
                        onClick = {
                            value = ""
                        },
                    ) {
                        Icon(
                            painterResource(R.drawable.clear),
                            contentDescription = stringResource(
                                R.string.clear_input
                            ),
                        )
                    }
            }
        )
        Spacer(Modifier.width(Density.ExtraSmall))
        IconButton (
            onClick = onOpen,
            modifier = Modifier.width(Density.IconSize)
        ) {
            Icon(
                painterResource(R.drawable.go),
                stringResource(R.string.open_deeplink)
            )
        }
    }
}

