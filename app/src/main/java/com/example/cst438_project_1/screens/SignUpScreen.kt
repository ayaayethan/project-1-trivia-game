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
import com.example.cst438_project_1.ui.components.GameButton
import com.example.cst438_project_1.ui.components.GameTextField

@Composable
fun SignUpScreen(
    errorMessage: String? = null,
    onSignUpClick: (String, String, String) -> Unit,
    onBackClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CREATE ACCOUNT",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(32.dp))

            GameTextField(value = username, onValueChange = { username = it }, label = "Pick a Username")

            GameTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            GameTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm Password",
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = password != confirmPassword && confirmPassword.isNotEmpty()
            )

            errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            GameButton(text = "Sign Up", onClick = { onSignUpClick(username, password, confirmPassword) })

            TextButton(onClick = onBackClick, modifier = Modifier.padding(top = 8.dp)) {
                Text("Already have an account? Go back")
            }
        }
    }
}