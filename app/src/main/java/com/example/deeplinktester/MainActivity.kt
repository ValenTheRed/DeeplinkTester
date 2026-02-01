package com.example.deeplinktester

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
    NavHost(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        navController = navController,
        startDestination = "home",
        // NOTE:
        //  1. enter/exit work when navigation *to* a screen and pop
        //  enter/exit work when navigation *back* from a screen.
        //  2. Enter is for dst screen and exit is for src screen. Vice versa
        //  for pop variations.
        //  3. Offset for pop exit variation is the point till which
        //  animation would take place. So, a `it/2` with fadeOut() pop exit
        //  will have the fade out for half screen and then disappear entirely.
        enterTransition = {
            slideInHorizontally(initialOffsetX = { it }) + fadeIn()
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { 0 }) + fadeOut()
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { 0 }) + fadeIn()
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
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