package io.github.ns200310.sol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import io.github.ns200310.sol.auth.controllers.AuthManager
import io.github.ns200310.sol.auth.screens.ForgotPasswordScreen
import io.github.ns200310.sol.auth.screens.LoginScreen
import io.github.ns200310.sol.auth.screens.RegisterScreen

import io.github.ns200310.sol.navigation.NavigationManager
import io.github.ns200310.sol.ui.theme.SOLTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SOLTheme {
                // check whether the user is logged in or not
                val authState by AuthManager().OnAuthStateChanged().collectAsState(initial = false)

                val navController = rememberNavController()

                    // Navigation class object
                    NavigationManager().AppNavHost(
                        navController = navController,
                        isLoggedIn = authState // Change this based on your authentication state
                    )


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