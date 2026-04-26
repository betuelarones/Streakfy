package com.streakfy.app.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.streakfy.app.ui.screens.tasks.TasksScreen

sealed class Screen(val route: String) {
    object Tasks : Screen("tasks")
    object Dascboard : Screen("dasboard")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Tasks.route ) {
        composable(Screen.Tasks.route) { TasksScreen() }
        composable(Screen.Dascboard.route) { Text("Dasboard (próximo)") }
    }
}