package com.example.deeplinktester.ui.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavHostController
import com.example.deeplinktester.R
import com.example.deeplinktester.ui.SnackbarController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navHostController: NavHostController,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val controller =
        SnackbarController(
            snackbarHostState = snackbarHostState,
            coroutineScope = rememberCoroutineScope(),
        )
    CompositionLocalProvider(ActiveSnackbarController provides controller) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.imePadding(),
                )
            },
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Input(
                    onSearch = {},
                    onBack = { navHostController.popBackStack() },
                    modifier = Modifier.padding(),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Input(
    onSearch: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by rememberSaveable { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = query,
        onValueChange = {
            query = it
            onSearch(it)
        },
        textStyle =
            MaterialTheme.typography.bodyLarge.copy(
                MaterialTheme.colorScheme.onSurface
            ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        modifier = modifier.fillMaxWidth(),
        decorationBox = { innerTextField ->
            TextFieldDefaults.DecorationBox(
                innerTextField = innerTextField,
                colors = TextFieldDefaults.colors(),
                value = query,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                placeholder = { Text("Search") },
                leadingIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription =
                                stringResource(R.string.go_back),
                        )
                    }
                },
                trailingIcon = {
                    if (!query.isEmpty()) {
                        IconButton(
                            onClick = { query = "" },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription =
                                    stringResource(R.string.clear_input),
                            )
                        }
                    }
                },
                container = {},
            )
        },
    )
}
