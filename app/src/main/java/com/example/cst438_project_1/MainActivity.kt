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
import androidx.lifecycle.lifecycleScope
import com.example.cst438_project_1.data.db.AppDatabase
import com.example.cst438_project_1.data.repository.AuthRepository
import com.example.cst438_project_1.ui.theme.Cst438project1Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CoroutineScope

class MainActivity : ComponentActivity() {

    private lateinit var repo: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Remove any test signUp calls you may have left here
        val db = AppDatabase.getInstance(applicationContext)
        repo = AuthRepository(db.userDao())

        enableEdgeToEdge()
        setContent {
            Cst438project1Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AuthScreen(
                        onLoginSuccess = { navigateToStartGame() },
                        repo = repo,
                        lifecycleActivity = this@MainActivity
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
fun AuthScreen(
    onLoginSuccess: () -> Unit,
    repo: AuthRepository,
    lifecycleActivity: ComponentActivity
) {
    // Use SnackbarHostState directly (Material3 friendly)
    val snackbarHostState = remember { SnackbarHostState() }
    val uiScope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var busyLogin by remember { mutableStateOf(false) }
    var busySignUp by remember { mutableStateOf(false) }

    Scaffold(
        // pass SnackbarHost to Scaffold in Material3
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome", style = MaterialTheme.typography.headlineMedium)
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

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        // LOGIN
                        if (username.isBlank() || password.isBlank()) {
                            uiScope.launch { snackbarHostState.showSnackbar("Please enter username and password") }
                            return@Button
                        }
                        busyLogin = true
                        lifecycleActivity.lifecycleScope.launch {
                            val res = withContext(Dispatchers.IO) {
                                repo.login(username.trim(), password)
                            }
                            busyLogin = false
                            res.fold(
                                onSuccess = {
                                    onLoginSuccess()
                                },
                                onFailure = {
                                    uiScope.launch { snackbarHostState.showSnackbar(it.message ?: "Login failed") }
                                }
                            )
                        }
                    },
                    enabled = !busyLogin && !busySignUp,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (busyLogin) "Logging in..." else "Login")
                }

                Spacer(Modifier.width(12.dp))

                OutlinedButton(
                    onClick = {
                        // SIGN UP
                        if (username.isBlank() || password.isBlank()) {
                            uiScope.launch { snackbarHostState.showSnackbar("Enter username and password to sign up") }
                            return@OutlinedButton
                        }
                        busySignUp = true
                        lifecycleActivity.lifecycleScope.launch {
                            val res = withContext(Dispatchers.IO) {
                                repo.signUp(username.trim(), password)
                            }
                            busySignUp = false
                            res.fold(
                                onSuccess = {
                                    uiScope.launch { snackbarHostState.showSnackbar("Account created. Please Login.") }
                                },
                                onFailure = {
                                    uiScope.launch { snackbarHostState.showSnackbar(it.message ?: "Sign up failed") }
                                }
                            )
                        }
                    },
                    enabled = !busySignUp && !busyLogin,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (busySignUp) "Signing up..." else "Sign Up")
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "Tip: sign up first with a username/password, then press Login.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}