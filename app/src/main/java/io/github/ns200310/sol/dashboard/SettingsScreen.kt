package io.github.ns200310.sol.dashboard

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.github.jan.supabase.auth.auth
import io.github.ns200310.sol.auth.controllers.AuthManager
import io.github.ns200310.sol.supabase_config.supabase
import kotlinx.coroutines.coroutineScope

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    navController: NavHostController
) {
    val darkModeState = remember { mutableStateOf(isDarkMode) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Dark Mode Toggle
        Text(text = "Dark Mode")
        Switch(
            checked = darkModeState.value,
            onCheckedChange = {
                darkModeState.value = it
                onToggleDarkMode(it)
            }
        )

        // Logout Button
        Button(
            onClick = {
                // Call the logout function from AuthManager

                try {
                    AuthManager().SignOutWithEmailAndPassword()
                    Toast.makeText(
                        navController.context,
                        "Logout successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Navigate to the login screen or perform any other action
                    navController.navigate("login")

                }
                catch (e: Exception) {
                    Toast.makeText(
                        navController.context,
                        "Logout failed: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }




            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Logout")
        }
    }
}