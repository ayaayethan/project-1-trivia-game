package com.example.cst438_project_1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cst438_project_1.data.db.AppDatabase
import com.example.cst438_project_1.data.repository.AuthRepository
import com.example.cst438_project_1.ui.theme.Cst438project1Theme
import com.example.cst438_project_1.viewmodels.GamesViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: GamesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Cst438project1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthNavigation()
                }
            }
        }

        // logs game data from API for debugging
         viewModel.debugFetchGames()
    }

    private fun onSuccessfulAuth() {
        val intent = Intent(this, StartGameActivity::class.java)
        startActivity(intent)
        finish()
    }

    @Composable
    fun AuthNavigation() {
        val navController = rememberNavController()
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        // Room DB on device
        val db = remember { AppDatabase.get(context) }
        val authRepo = remember { AuthRepository(db.userDao()) }

        var loginError by remember { mutableStateOf<String?>(null) }
        var signUpError by remember { mutableStateOf<String?>(null) }

        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                LoginScreen(
                    errorMessage = loginError,
                    onLoginClick = { username, password ->
                        loginError = null
                        scope.launch {
                            val res = authRepo.login(username, password)
                            if (res.isSuccess) {
                                onSuccessfulAuth()
                            } else {
                                loginError = res.exceptionOrNull()?.message ?: "Login failed"
                            }
                        }
                    },
                    onSignUpClick = { navController.navigate("signup") }
                )
            }
            composable("signup") {
                SignUpScreen(
                    errorMessage = signUpError,
                    onSignUpClick = { username, password, confirmPassword ->
                        signUpError = null
                        if (password != confirmPassword) {
                            signUpError = "Passwords do not match"
                        } else {
                            scope.launch {
                                val res = authRepo.createAccount(username, password)
                                if (res.isSuccess) {
                                    onSuccessfulAuth()
                                } else {
                                    signUpError = res.exceptionOrNull()?.message ?: "Sign up failed"
                                }
                            }
                        }
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    onLoginClick: (String, String) -> Unit,
    onSignUpClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onLoginClick(username, password) }) {
            Text(text = "Login")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onSignUpClick) {
            Text(text = "Sign Up")
        }
    }
}

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    onSignUpClick: (String, String, String) -> Unit,
    onBackClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Sign Up", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onSignUpClick(username, password, confirmPassword) }) {
            Text(text = "Sign Up")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onBackClick) {
            Text(text = "Back")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Cst438project1Theme {
        LoginScreen(onLoginClick = { _, _ -> }, onSignUpClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    Cst438project1Theme {
        SignUpScreen(onSignUpClick = { _, _, _ -> }, onBackClick = {})
    }
}