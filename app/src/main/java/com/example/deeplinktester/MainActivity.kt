package com.example.deeplinktester

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.deeplinktester.data.DataStoreInstance.dataStore
import com.example.deeplinktester.ui.AppViewModel
import com.example.deeplinktester.ui.SnackbarController
import com.example.deeplinktester.ui.components.HistoryList
import com.example.deeplinktester.ui.components.Input
import com.example.deeplinktester.ui.theme.DeeplinkTesterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeeplinkTesterTheme {
                App(
                    viewModel {
                        AppViewModel(dataStore = applicationContext.dataStore)
                    }
                )
            }
        }
    }
}

val ActiveSnackbarController =
    compositionLocalOf<SnackbarController> {
        error("No snackbar controller found!")
    }

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun App(appViewModel: AppViewModel = viewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val appUiState by appViewModel.uiState.collectAsState()
    val controller =
        SnackbarController(
            snackbarHostState = snackbarHostState,
            coroutineScope = rememberCoroutineScope(),
        )

    CompositionLocalProvider(ActiveSnackbarController provides controller) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = { Text(stringResource(R.string.app_name)) })
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.imePadding(),
                )
            },
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Input(
                    onOpenDeeplink = { deeplink ->
                        appViewModel.push(deeplink)
                    },
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                HistoryList(
                    appUiState.list,
                    { index -> appViewModel.delete(index) },
                    paddingFromEdge = 16.dp,
                )
            }
        }
    }
}