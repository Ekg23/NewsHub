package com.example.newshub

// Bringing in our 3 screens and the Routes object so we can reference them below
import com.example.newshub.screens.HomeScreen
import com.example.newshub.screens.ArticleScreen
import com.example.newshub.screens.ProfileScreen
import com.example.newshub.screens.Routes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost      // container that holds/swaps the current screen
import androidx.navigation.compose.composable   // declares one destination inside the NavHost
import androidx.navigation.compose.rememberNavController // creates/remembers the controller that drives navigation
import com.example.newshub.ui.theme.NewsHubTheme

// Every Android app needs an "Activity" as its entry point.
// This one hosts our entire Compose UI.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // lets our UI draw behind the system status/nav bars for a modern look

        // setContent {} is where we describe our Compose UI
        setContent {
            NewsHubTheme { // applies our app's colors/typography to everything inside
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // navController is the "remote control" that lets any screen
                    // trigger a move to another screen.
                    val navController = rememberNavController()

                    // NavHost is the "stage" — it shows exactly one screen at a time,
                    // starting at Routes.HOME, and swaps the content when navigate() is called.
                    NavHost(
                        navController = navController,
                        startDestination = Routes.HOME,
                        modifier = Modifier.padding(innerPadding) // avoid drawing under system bars
                    ) {
                        // Each composable() line maps a route name to the actual screen function
                        composable(Routes.HOME) { HomeScreen(navController) }
                        composable(Routes.ARTICLE) { ArticleScreen(navController) }
                        composable(Routes.PROFILE) { ProfileScreen(navController) }
                    }
                }
            }
        }
    }
}