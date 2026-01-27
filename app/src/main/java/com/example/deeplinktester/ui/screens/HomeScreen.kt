package com.example.deeplinktester.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.deeplinktester.R
import com.example.deeplinktester.ui.HomeModel
import com.example.deeplinktester.ui.SnackbarController
import com.example.deeplinktester.ui.components.HistoryItem
import com.example.deeplinktester.ui.components.HistoryList
import com.example.deeplinktester.ui.components.Input
import com.example.deeplinktester.ui.theme.Density
import com.example.deeplinktester.ui.theme.appEdgePadding

val ActiveSnackbarController =
    compositionLocalOf<SnackbarController> {
        error("No snackbar controller found!")
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    homeModel: HomeModel,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val deeplinks by homeModel.deeplinks.collectAsState()
    val controller =
        SnackbarController(
            snackbarHostState = snackbarHostState,
            coroutineScope = rememberCoroutineScope(),
        )
    CompositionLocalProvider(ActiveSnackbarController provides controller) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Column(
                    modifier = Modifier.padding(bottom = Density.Small)
                ) {
                    TopAppBar(
                        title = { Text(stringResource(R.string.app_name)) },
                        actions = {
                            IconButton(
                                onClick = { navHostController.navigate("search") },
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.search),
                                    contentDescription = stringResource(R.string.search_deeplinks),
                                )
                            }

                        }
                    )
                    Input(
                        onOpenDeeplink = { deeplink ->
                            homeModel.push(deeplink)
                        },
                        modifier = Modifier.appEdgePadding(),
                    )
                }
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.imePadding(),
                )
            },
        ) { innerPadding ->
            HistoryList(
                data = deeplinks,
                modifier = Modifier.padding(innerPadding),
            ) { deeplink, index, modifier ->
                HistoryItem(
                    deeplink,
                    onDelete = { homeModel.delete(index) },
                    modifier = modifier
                )
            }
        }
    }
}
