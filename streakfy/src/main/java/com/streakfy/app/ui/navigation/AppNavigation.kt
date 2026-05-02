package com.streakfy.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.streakfy.app.data.local.database.DatabaseProvider
import com.streakfy.app.ui.screens.calendar.CalendarScreen
import com.streakfy.app.ui.screens.dashboard.DashboardScreen
import com.streakfy.app.ui.screens.focus.FocusScreen
import com.streakfy.app.ui.screens.profile.ProfileScreen
import com.streakfy.app.ui.screens.statistics.StatisticsScreen
import com.streakfy.app.ui.screens.streak.StreakScreen
import com.streakfy.app.ui.screens.tasks.TasksScreen
import com.streakfy.app.ui.theme.StreakFyTheme

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Inicio", Icons.Default.Home)
    object Tasks : Screen("tasks", "Tareas", Icons.Default.List)
    object Calendar : Screen("calendar", "Calendario", Icons.Default.DateRange)
    object Statistics : Screen("statistics", "Estadísticas", Icons.Default.Star)
    object Profile : Screen("profile", "Perfil", Icons.Default.Person)
}

@Composable
fun AppNavigation() {
    StreakFyTheme {
        val navController = rememberNavController()
        val context = LocalContext.current
        val db = remember { DatabaseProvider.getDatabase(context) }

        val items = listOf(
            Screen.Dashboard,
            Screen.Tasks,
            Screen.Calendar,
            Screen.Statistics,
            Screen.Profile
        )

        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = androidx.compose.ui.graphics.Color(0xFF1A1A1A),
                    contentColor = androidx.compose.ui.graphics.Color(0xFFE5E5E5)
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("focus") }
                ) {
                    Icon(Icons.Default.Star, contentDescription = "Iniciar Foco")
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Dashboard.route
                ) {
                    composable(Screen.Dashboard.route) {
                        DashboardScreen(
                            onGoToTasks = { navController.navigate(Screen.Tasks.route) },
                            onStartFocus = { navController.navigate("focus") }
                        )
                    }
                    composable(Screen.Tasks.route) { TasksScreen() }
                    composable(Screen.Calendar.route) { CalendarScreen() }
                    composable(Screen.Statistics.route) { StatisticsScreen() }
                    composable(Screen.Profile.route) { ProfileScreen() }
                    composable("focus") { FocusScreen(onBack = { navController.popBackStack() }) }
                    composable("streak") { StreakScreen() }
                }
            }
        }
    }
}
