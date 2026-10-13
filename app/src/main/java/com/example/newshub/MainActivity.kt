package com.example.newshub

// Bringing in our screens and the Routes object so we can reference them below
import com.example.newshub.screens.HomeScreen
import com.example.newshub.screens.ArticleScreen
import com.example.newshub.screens.ProfileScreen
import com.example.newshub.screens.SavedScreen
import com.example.newshub.screens.AuthScreen
import com.example.newshub.screens.Routes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost      // container that holds/swaps the current screen
import androidx.navigation.compose.composable   // declares one destination inside the NavHost
import androidx.navigation.compose.rememberNavController // creates/remembers the controller that drives navigation
import com.example.newshub.ui.theme.NewsHubTheme

// Every Android app needs an "Activity" as its entry point.
// This one hosts our entire Compose UI.
class MainActivity : ComponentActivity() {

    // Creates the AuthViewModel once and keeps it for the life of this activity
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // lets our UI draw behind the system status/nav bars

        // setContent {} is where we describe our Compose UI
        setContent {
            NewsHubTheme {
                if (authViewModel.isLoggedIn) {
                    // Signed in: show the normal app
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = Routes.HOME,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(Routes.HOME) { HomeScreen(navController) }
                            composable(Routes.ARTICLE) { ArticleScreen(navController) }
                            composable(Routes.PROFILE) { ProfileScreen(navController, authViewModel) }

                            // NEW: the saved articles screen, reached with navigate("saved")
                            composable(Routes.SAVED) { SavedScreen(navController) }
                        }
                    }
                } else {
                    // Not signed in: show the login / sign up screen
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            AuthScreen(authViewModel)
                        }
                    }
                }
            }
        }
    }
}