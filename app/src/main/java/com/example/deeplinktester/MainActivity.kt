package com.example.deeplinktester

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.deeplinktester.ui.theme.DeeplinkTesterTheme
import com.example.deeplinktester.utils.debounce
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "history"
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeeplinkTesterTheme {
                App()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun App() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Deeplink Tester") },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Input(
                showSnackbar = { message ->
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = message
                        )
                    }
                }
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp)
            )
            HistoryList(List(15) {
                "myairtel://app/react?screenName=generic-nav-stack" +
                        "&selectedPage=paybill_category_landing&subCategory" +
                        "=electricity"
            })
        }
    }
}

@Composable
fun Input(
    showSnackbar: (message: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var value by remember { mutableStateOf("") }
    val ctx = LocalContext.current

    Column(
        modifier = modifier,
    ) {
        TextField(
            value = value,
            onValueChange = { text ->
                value = text
            },
            placeholder = { Text("deeplink://") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = debounce(400, {
                if (value.trim() == "") {
                    return@debounce
                }
                try {
                    ctx.startActivity(Intent(Intent.ACTION_VIEW, value.toUri()))
                } catch (_: ActivityNotFoundException) {
                    showSnackbar("No app exists for the deeplink")
                }
            }),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Open Deeplink")
        }
    }
}

@Composable
fun HistoryList(
    data: List<String>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item(key = "history_heading") {
            Text(
                text = "History",
                style = MaterialTheme.typography.titleLarge
            )
        }
        items(data) { d ->
            DeeplinkItem(d)
        }
    }
}


@Composable
fun DeeplinkItem(
    deeplink: String,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = deeplink,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )
        IconButton(
            onClick = {},
        ) {
            Icon(
                painter = painterResource(R.drawable.content_copy),
                contentDescription = "Copy deeplink",
            )
        }
        IconButton(
            onClick = {},
        ) {
            Icon(
                painter = painterResource(R.drawable.delete),
                contentDescription = "Delete deeplink",
            )
        }
    }
}