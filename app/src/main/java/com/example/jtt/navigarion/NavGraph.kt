package com.example.jtt.navigarion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jtt.ui.theme.screens.ConfessionScreen
import com.example.jtt.ui.theme.splash.SplashScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("confession") {
            ConfessionScreen()
        }
    }
}