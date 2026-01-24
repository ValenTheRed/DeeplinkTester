package com.example.deeplinktester.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.deeplinktester.R
import com.example.deeplinktester.ui.SearchModel
import com.example.deeplinktester.ui.SnackbarController
import com.example.deeplinktester.ui.components.HistoryItem

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
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.imePadding(),
                )
            },
        ) { innerPadding ->
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                item(key = "search_input") {
                    Search(
                        query = searchModel.query,
                        onSearch = {
                            searchModel.onSearch(it)
                        },
                        onBack = { navHostController.popBackStack() },
                        modifier = Modifier.padding(end = 16.dp),
                    )
                }
                itemsIndexed(searchResults) { index, link ->
                    HistoryItem(
                        deeplink = link,
                        paddingFromEdge = 16.dp,
                    )
                    if (index < searchResults.size - 1) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 0.dp),
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(
    query: String,
    onSearch: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription =
                    stringResource(R.string.go_back),
            )
        }
        TextField(
            value = query,
            onValueChange = onSearch,
            placeholder = { Text(stringResource(R.string.search)) },
            trailingIcon = {
                if (!query.isEmpty()) {
                    IconButton(
                        onClick = { onSearch("") },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription =
                                stringResource(R.string.clear_input),
                        )
                    }
                }
            },
            shape = ShapeDefaults.ExtraLarge,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch(query) }
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}
