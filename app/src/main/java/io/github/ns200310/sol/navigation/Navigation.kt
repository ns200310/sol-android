package io.github.ns200310.sol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import io.github.ns200310.sol.auth.screens.ForgotPasswordScreen
import io.github.ns200310.sol.auth.screens.LoginScreen
import io.github.ns200310.sol.auth.screens.RegisterScreen
import io.github.ns200310.sol.dashboard.DashboardTabNav


class NavigationManager {
    @Composable
    fun AppNavHost(
        navController: NavHostController = rememberNavController(),
        isLoggedIn: Boolean,
        isDarkMode: MutableState<Boolean>,
    ) {
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) "main" else "auth"
        ) {
            authNavGraph(navController)
            mainNavGraph(navController, isDarkMode)
        }
    }
    // auth
    fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
        navigation(startDestination = "login", route = "auth") {
            composable("login") { LoginScreen(navController) }
            composable("register") { RegisterScreen(navController) }
            composable("forgot_password") { ForgotPasswordScreen(navController) }
        }
    }

    // main
    fun NavGraphBuilder.mainNavGraph(navController: NavHostController, isDarkMode: MutableState<Boolean>) {
        navigation(startDestination = "home_root", route = "main") {
            composable("home_root") { DashboardTabNav(AppHostNavController = navController, darkTheme = isDarkMode) }

        }
    }
}