package com.example.deeplinktester

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.deeplinktester.data.DataStoreInstance.dataStore
import com.example.deeplinktester.ui.HomeModel
import com.example.deeplinktester.ui.SearchModel
import com.example.deeplinktester.ui.screens.HomeScreen
import com.example.deeplinktester.ui.screens.SearchScreen
import com.example.deeplinktester.ui.theme.DeeplinkTesterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeeplinkTesterTheme {
                val homeModel = viewModel {
                    HomeModel(dataStore = applicationContext.dataStore)
                }
                val searchModel = viewModel {
                    SearchModel(
                        dataStore = applicationContext.dataStore,
                        deeplinks = homeModel.deeplinks
                    )
                }
                App(homeModel, searchModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun App(
    homeModel: HomeModel = viewModel(),
    searchModel: SearchModel = viewModel()
) {
    val navController = rememberNavController()
    // NOTE: without this `Surface`, you can see a white flash inbetween the
    //  transitions.
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = "home",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut()
            }
        ) {
            composable("home") {
                HomeScreen(navController, homeModel)
            }
            composable("search") {
                SearchScreen(navController, searchModel)
            }
        }
    }
}