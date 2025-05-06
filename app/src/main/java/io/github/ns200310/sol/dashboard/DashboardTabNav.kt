package io.github.ns200310.sol.dashboard

import HomeScreen
import MeterScreen
import NewsDetails
import SettingsScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun DashboardTabNav(AppHostNavController: NavHostController,  darkTheme: MutableState<Boolean>) {
    val navController = rememberNavController()
    val items = listOf("home", "meter", "settings")
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
                                "meter" -> {
                                    Icon(
                                        imageVector = Icons.Default.Speed,
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
            composable("settings") { SettingsScreen(
                onToggleDarkMode = {
                    // Handle dark mode toggle

                },
                // check device color scheme
                isDarkMode = darkTheme,
                navController = AppHostNavController

            ) }
            composable("meter") {
                // Chat screen content
                MeterScreen(navController = navController)

            }
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

