package com.streakfy.app.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.streakfy.app.data.local.database.DatabaseProvider
import com.streakfy.app.data.repository.TaskRepository
import com.streakfy.app.ui.screens.dashboard.DashboardScreen
import com.streakfy.app.ui.screens.tasks.TasksScreen

sealed class Screen(val route: String) {
    object Tasks : Screen("tasks")
    object Dascboard : Screen("dasboard")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val db = remember { DatabaseProvider.getDatabase(context) }
    val repo = remember { TaskRepository(db.taskDao()) }

    val tasks by repo.getTasks().collectAsState(initial = emptyList())

    NavHost(navController = navController, startDestination = Screen.Tasks.route ) {
        composable(Screen.Tasks.route) { TasksScreen() }

        composable(Screen.Dascboard.route) {
            DashboardScreen(
                onGoToTasks = {
                    navController.navigate(Screen.Tasks.route)
                }
            )
        }
    }
}