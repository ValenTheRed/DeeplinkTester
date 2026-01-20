package com.example.deeplinktester

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.deeplinktester.data.DataStoreInstance.dataStore
import com.example.deeplinktester.ui.AppViewModel
import com.example.deeplinktester.ui.screens.HomeScreen
import com.example.deeplinktester.ui.screens.SearchScreen
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun App(appViewModel: AppViewModel = viewModel()) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController, appViewModel)
        }
        composable("search") {
            SearchScreen(navController)
        }
    }
}