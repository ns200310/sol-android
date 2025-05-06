import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.github.ns200310.sol.auth.controllers.AuthManager

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    isDarkMode: MutableState<Boolean>,
    onToggleDarkMode: (Boolean) -> Unit,
    navController: NavHostController
) {
    val darkModeState = remember { mutableStateOf(isDarkMode) }
    val notificationsEnabled = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)

    ) {
        Row (
            modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            // Dark Mode Toggle
            Text(text = "Dark Mode")
            Switch(
                checked = isDarkMode.value,
                onCheckedChange = {
                    isDarkMode.value = it
                    onToggleDarkMode(it)
                }
            )
        }






        Row (
            modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {

            // Logout Button
            Button(

                onClick = {
                    try {
                        AuthManager().SignOutWithEmailAndPassword()
                        Toast.makeText(
                            navController.context,
                            "Logout successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate("login")
                    } catch (e: Exception) {
                        Toast.makeText(
                            navController.context,
                            "Logout failed: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                colors = ButtonColors(
                    containerColor = Color(0xFFE91E63),     // Background color
                    contentColor = Color.White,             // Text color
                    disabledContainerColor = Color.Gray,    // Disabled background
                    disabledContentColor = Color.LightGray  // Disabled text
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()

            ) {
                Text(text = "Logout")
            }

        }



    }
}