package com.example.cst438_project_1.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cst438_project_1.screens.GameScreen
import com.example.cst438_project_1.screens.MainMenuScreen


@Composable
fun AppNavigation(onLogout: () -> Unit) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main_menu") {
        composable("main_menu") {
            MainMenuScreen(
                onPlayClick = { navController.navigate("game_screen") },
                onLogoutClick = onLogout
            )
        }
        composable("game_screen") {
            GameScreen(onQuitClick = { navController.popBackStack() })
        }
    }
}