package io.github.ns200310.sol

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import io.github.ns200310.sol.auth.controllers.AuthManager
import io.github.ns200310.sol.auth.screens.ForgotPasswordScreen
import io.github.ns200310.sol.auth.screens.LoginScreen
import io.github.ns200310.sol.auth.screens.RegisterScreen
import io.github.ns200310.sol.dashboard.LoadingScreen

import io.github.ns200310.sol.navigation.NavigationManager
import io.github.ns200310.sol.network.NetworkStatus
import io.github.ns200310.sol.network.Offline
import io.github.ns200310.sol.ui.theme.SOLTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val networkClasssss = io.github.ns200310.sol.network.NetworkStatus(this)

        enableEdgeToEdge()
        setContent {
            val isDarkMode = remember { mutableStateOf(false) }

            SOLTheme(darkTheme = isDarkMode.value) {

                // Track loading state
                var isLoading by remember { mutableStateOf(true) }
                val authState by AuthManager().OnAuthStateChanged().collectAsState(initial = false)
                val isConnected by networkClasssss.isConnected.collectAsState()

                // Update loading state when authState is resolved
                LaunchedEffect (authState) {
                    delay(2000)
                    isLoading = false
                }

                val navController = rememberNavController()

                if (isLoading) {
                    // Show loading screen
                    LoadingScreen()
                } else {
                    // Show the main content
                    if (isConnected) {


                        NavigationManager().AppNavHost(
                            navController = navController,
                            isLoggedIn = authState,
                            isDarkMode = isDarkMode,
                        )
                    }
                    else {
                        Offline()
                    }

                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SOLTheme {
        Greeting("Android")
    }
}