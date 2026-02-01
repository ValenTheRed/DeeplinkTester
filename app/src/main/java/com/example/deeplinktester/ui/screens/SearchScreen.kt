package com.example.deeplinktester.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavHostController
import com.example.deeplinktester.R
import com.example.deeplinktester.ui.SearchModel
import com.example.deeplinktester.ui.SearchResults
import com.example.deeplinktester.ui.SnackbarController
import com.example.deeplinktester.ui.components.HistoryItem
import com.example.deeplinktester.ui.components.HistoryList
import com.example.deeplinktester.ui.components.SearchResultQuery
import com.example.deeplinktester.ui.theme.AppEdgeType
import com.example.deeplinktester.ui.theme.Density
import com.example.deeplinktester.ui.theme.appEdgePadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navHostController: NavHostController,
    searchModel: SearchModel,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val searchResults by searchModel.searchResults.collectAsState()

    val controller =
        SnackbarController(
            snackbarHostState = snackbarHostState,
            coroutineScope = rememberCoroutineScope(),
        )
    CompositionLocalProvider(ActiveSnackbarController provides controller) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Search(
                    query = searchModel.query,
                    onSearch = {
                        searchModel.onSearch(it)
                    },
                    onKeyboardAction = {
                        searchModel.push()
                    },
                    onBack = { navHostController.popBackStack() },
                    modifier = Modifier
                        .padding(top = Density.Small, bottom = Density.Medium)
                        .appEdgePadding(pad = AppEdgeType.End)
                        .statusBarsPadding(),
                )
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.imePadding(),
                )
            },
        ) { innerPadding ->
            val searchResults = searchResults
            val results = when (searchResults) {
                SearchResults.EmptyQueries -> listOf()
                SearchResults.EmptyLinks -> listOf(
                    stringResource(R.string.no_results_found)
                )
                is SearchResults.Links -> searchResults.links
                is SearchResults.Queries -> searchResults.queries
            }
            HistoryList(
                data = results,
                modifier = Modifier.padding(innerPadding),
            ) { result, index, modifier ->
                when (searchResults) {
                    SearchResults.EmptyQueries -> {}
                    SearchResults.EmptyLinks -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = modifier
                                .fillParentMaxSize()
                                .imePadding(),
                        ) {
                            Text(
                                result,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }

                    is SearchResults.Links -> {
                        HistoryItem(
                            deeplink = result,
                            highlightedDeeplink = searchModel.highlightDeeplink(
                                result,
                                SpanStyle(
                                    color = MaterialTheme.colorScheme.surfaceTint,
                                    textDecoration = TextDecoration.Underline,
                                )
                            ),
                            modifier = modifier,
                        )
                    }

                    is SearchResults.Queries -> SearchResultQuery(
                        result,
                        onDelete = { searchModel.delete(result) },
                        onUndo = { searchModel.push(result, index) },
                        onClick = { searchModel.onSearch(result) },
                        modifier = modifier,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(
    query: TextFieldState,
    onSearch: (String) -> Unit,
    onKeyboardAction: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusManager.moveFocus(focusDirection = FocusDirection.Previous)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        IconButton(onClick = onBack) {
            Icon(
                painterResource(R.drawable.go_back),
                contentDescription =
                    stringResource(R.string.go_back),
            )
        }
        TextField(
            state = query,
            placeholder = { Text(stringResource(R.string.search)) },
            trailingIcon = {
                if (query.text.isEmpty()) {
                    return@TextField
                }
                IconButton(
                    onClick = { onSearch("") },
                ) {
                    Icon(
                        painterResource(R.drawable.clear),
                        contentDescription =
                            stringResource(R.string.clear_input),
                    )
                }
            },
            shape = ShapeDefaults.ExtraLarge,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            onKeyboardAction = {
                onKeyboardAction()
                keyboardController?.hide()
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
