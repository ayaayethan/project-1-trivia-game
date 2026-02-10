package com.example.cst438_project_1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cst438_project_1.ui.components.GameButton
import com.example.cst438_project_1.ui.components.GameTextField

@Composable
fun LoginScreen(
    errorMessage: String? = null,
    onLoginClick: (String, String) -> Unit,
    onSignUpClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Branding Section
            Text(
                text = "GAME DIFF",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 4.sp
                )
            )
            Text(
                text = "Show your gaming IQ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Input Section
            GameTextField(
                value = username,
                onValueChange = { username = it },
                label = "Username"
            )

            GameTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action Section
            GameButton(text = "Login", onClick = { onLoginClick(username, password) })

            TextButton(onClick = onSignUpClick, modifier = Modifier.padding(top = 16.dp)) {
                Text("New player? Create an account")
            }
        }
    }
}