package com.example.projecthehehe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.projecthehehe.ui.screens.*
import com.example.projecthehehe.ui.theme.ProjectheheheTheme
import com.example.projecthehehe.viewmodel.CalorieTrackerViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectheheheTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: CalorieTrackerViewModel = viewModel()
    val items = listOf(
        Screen.Profile,
        Screen.Tracker
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
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
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Profile.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Profile.route) {
                UserProfileScreen(
                    onNavigateToTracker = {
                        navController.navigate(Screen.Tracker.route) {
                            popUpTo(Screen.Profile.route) { inclusive = true }
                        }
                    },
                    viewModel = viewModel
                )
            }
            composable(Screen.Tracker.route) {
                CalorieTrackerScreen(viewModel = viewModel)
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    object Tracker : Screen("tracker", "Tracker", Icons.Default.List)
}
