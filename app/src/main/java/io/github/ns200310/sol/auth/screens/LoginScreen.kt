package io.github.ns200310.sol.auth.screens

import android.app.AlertDialog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.ns200310.sol.R
import io.github.ns200310.sol.auth.controllers.AuthManager
import io.github.ns200310.sol.auth.controllers.AuthResponse
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(nav: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var responseMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Add the image at the top

        Image(
            painter = painterResource(id = R.drawable.login_img), // Replace with your image resource
            contentDescription = "Login Image",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f) // Adjust height as needed
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Sign In",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 32.sp,
            modifier = Modifier.padding(start = 16.dp).align(Alignment.Start)
        )
        Text(
            "Enter your email and password to login",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 32.dp, start = 16.dp).align(Alignment.Start)
        )

        OutlinedTextField(

            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible) "Hide" else "Show"
                TextButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(icon)
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth(0.9f)
        )


        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val authManager = AuthManager()
                coroutineScope.launch {
                    authManager.SignInWithEmailAndPassword(email, password).collect { response ->
                        when (response) {
                            is AuthResponse.Success -> responseMessage = "Login successful!"
                            is AuthResponse.Error -> responseMessage = response.message
                        }
                        // AlertDialog to show the response message
                        if (responseMessage.isNotEmpty()) {
                            Toast.makeText(context, responseMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            },
            modifier = Modifier.fillMaxWidth(0.9f)

        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = {
                nav.navigate("forgot_password")
            }) {
                Text("Forgot Password?")
            }
            TextButton(onClick = {
                nav.navigate("register")
            }) {
                Text("Sign Up")
            }
        }
    }
}



