package io.github.ns200310.sol.dashboard

import HomeScreen
import NewsDetails
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.ns200310.sol.auth.controllers.AuthManager
import io.github.ns200310.sol.auth.controllers.AuthResponse
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun DashboardTabNav(AppHostNavController: NavHostController) {
    val navController = rememberNavController()
    val items = listOf("home", "profile", "settings")
    val selectedNavigationIndex = rememberSaveable {
        mutableIntStateOf(0)
    }


    Scaffold(
        bottomBar = {
            BottomAppBar {
                items.forEach { screen ->

                    NavigationBarItem(
                        icon = {
                            // Icon for each screen
                            when (screen) {
                                "home" -> {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = null
                                    )
                                }
                                "profile" -> {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null
                                    )
                                }
                                "settings" -> {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = null
                                    )
                                }
                            }

                        },
                        label = { Text(screen) },
                        selected =  selectedNavigationIndex.intValue == items.indexOf(screen),
                        onClick = {
                            selectedNavigationIndex.intValue = items.indexOf(screen)
                            // Handle navigation to the selected screen
                            navController.navigate(screen) {
                                // Clear the back stack if needed
                                popUpTo("home") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )

                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable("home") { HomeScreen(navController=navController) }
            composable("profile") { ProfileScreen() }
            composable("settings") { SettingsScreen(
                onToggleDarkMode = {
                    // Handle dark mode toggle
                },
                isDarkMode = false,
                navController = AppHostNavController

            ) }
            composable("news/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")

                if (id != null) {
                    NewsDetails(
                        id = id.toInt(),
                        navigationController = navController,
                     )
                }
            }
        }
    }
}

