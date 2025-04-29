package io.github.ns200310.sol.auth.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun ForgotPasswordScreen(nav: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Add the image at the top

        Image(
            painter = painterResource(id = R.drawable.forgot_password), // Replace with your image resource
            contentDescription = "Register Image",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f) // Adjust height as needed
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Forgot Password",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 32.sp,
            modifier = Modifier.padding( start = 16.dp).align(Alignment.Start)
        )
        Text("Enter your email to reset your password",
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



        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                var resetP = AuthManager().ResetPassword(email)
                Toast.makeText(nav.context, resetP.toString(), Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Text("Send Reset Link")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { nav.navigate("register") }) {
                Text("Sign Up")
            }
            TextButton(onClick = { nav.navigate("login") }) {
                Text("Sign In")
            }
        }
    }
}