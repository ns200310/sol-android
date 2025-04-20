package io.github.ns200310.sol.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun DashboardTabNav() {
    val navController = rememberNavController()
    val items = listOf("home", "profile", "settings")
    Text("Dashboard")


}
