package com.example.cst438_project_1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cst438_project_1.data.db.AppDatabase
import com.example.cst438_project_1.data.repository.AuthRepository
import com.example.cst438_project_1.ui.theme.Cst438project1Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private lateinit var repo: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getInstance(applicationContext)
        repo = AuthRepository(db.userDao())

        enableEdgeToEdge()
        setContent {
            Cst438project1Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AuthNavGraph(
                        repo = repo,
                        onLoginSuccess = { navigateToStartGame() }
                    )
                }
            }
        }
    }

    private fun navigateToStartGame() {
        startActivity(Intent(this, StartGameActivity::class.java))
        finish()
    }
}

@Composable
private fun AuthNavGraph(
    repo: AuthRepository,
    onLoginSuccess: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                repo = repo,
                onLoginSuccess = onLoginSuccess,
                onGoToSignUp = { navController.navigate("signup") }
            )
        }
        composable("signup") {
            SignUpScreen(
                repo = repo,
                onSignUpDoneGoToLogin = {
                    // Force login screen to be recreated (clears any old login state)
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onBackToLogin = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun LoginScreen(
    repo: AuthRepository,
    onLoginSuccess: () -> Unit,
    onGoToSignUp: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var busy by remember { mutableStateOf(false) }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Login", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val u = username.trim()
                    if (u.isBlank() || password.isBlank()) {
                        scope.launch { snackbarHostState.showSnackbar("Enter username and password") }
                        return@Button
                    }

                    busy = true
                    scope.launch {
                        val res = withContext(Dispatchers.IO) { repo.login(u, password) }
                        busy = false
                        res.fold(
                            onSuccess = { onLoginSuccess() },
                            onFailure = { snackbarHostState.showSnackbar(it.message ?: "Login failed") }
                        )
                    }
                },
                enabled = !busy,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (busy) "Logging in..." else "Login")
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onGoToSignUp,
                enabled = !busy,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Don’t have an account? Sign Up")
            }
        }
    }
}

@Composable
private fun SignUpScreen(
    repo: AuthRepository,
    onSignUpDoneGoToLogin: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var busy by remember { mutableStateOf(false) }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Sign Up", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val u = username.trim()
                    if (u.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                        scope.launch { snackbarHostState.showSnackbar("Fill out all fields") }
                        return@Button
                    }
                    if (password != confirmPassword) {
                        scope.launch { snackbarHostState.showSnackbar("Passwords do not match") }
                        return@Button
                    }

                    busy = true
                    scope.launch {
                        val res = withContext(Dispatchers.IO) { repo.signUp(u, password) }
                        busy = false
                        res.fold(
                            onSuccess = {
                                snackbarHostState.showSnackbar("Account created. Please log in.")
                                onSignUpDoneGoToLogin()
                            },
                            onFailure = {
                                snackbarHostState.showSnackbar(it.message ?: "Sign up failed")
                            }
                        )
                    }
                },
                enabled = !busy,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (busy) "Creating..." else "Create Account")
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBackToLogin,
                enabled = !busy,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Login")
            }
        }
    }
}