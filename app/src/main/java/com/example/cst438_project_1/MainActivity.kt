package com.example.cst438_project_1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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

private const val ROUTE_LOGIN = "login"
private const val ROUTE_SIGNUP = "signup"

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

    NavHost(navController = navController, startDestination = ROUTE_LOGIN) {
        composable(ROUTE_LOGIN) {
            LoginScreen(
                repo = repo,
                onLoginSuccess = onLoginSuccess,
                onGoToSignUp = { navController.navigate(ROUTE_SIGNUP) }
            )
        }
        composable(ROUTE_SIGNUP) {
            SignUpScreen(
                repo = repo,
                onSignUpDoneGoToLogin = {
                    navController.navigate(ROUTE_LOGIN) {
                        popUpTo(ROUTE_LOGIN) { inclusive = true }
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
    var showPassword by remember { mutableStateOf(false) }
    var busy by remember { mutableStateOf(false) }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { inner ->
        AuthBackground(modifier = Modifier.padding(inner)) {
            AuthCard(
                header = "Welcome back",
                subheader = "Log in to start playing"
            ) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Username") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Password") },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    trailingIcon = {
                        TextButton(onClick = { showPassword = !showPassword }) {
                            Text(if (showPassword) "Hide" else "Show")
                        }
                    }
                )

                Spacer(Modifier.height(18.dp))

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
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(if (busy) "Logging in..." else "Login")
                }

                Spacer(Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onGoToSignUp,
                    enabled = !busy,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Don’t have an account? Sign up")
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    "Accounts are stored locally (Room) on this device.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
    var showPassword by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }
    var busy by remember { mutableStateOf(false) }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { inner ->
        AuthBackground(modifier = Modifier.padding(inner)) {
            AuthCard(
                header = "Create account",
                subheader = "After signup you’ll return to Login"
            ) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Username") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Password (min 6 chars)") },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    trailingIcon = {
                        TextButton(onClick = { showPassword = !showPassword }) {
                            Text(if (showPassword) "Hide" else "Show")
                        }
                    }
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Confirm password") },
                    visualTransformation = if (showConfirm) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    trailingIcon = {
                        TextButton(onClick = { showConfirm = !showConfirm }) {
                            Text(if (showConfirm) "Hide" else "Show")
                        }
                    }
                )

                Spacer(Modifier.height(18.dp))

                Button(
                    onClick = {
                        val u = username.trim()
                        if (u.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                            scope.launch { snackbarHostState.showSnackbar("Fill out all fields") }
                            return@Button
                        }
                        if (password.length < 6) {
                            scope.launch { snackbarHostState.showSnackbar("Password must be at least 6 characters") }
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
                                onFailure = { snackbarHostState.showSnackbar(it.message ?: "Sign up failed") }
                            )
                        }
                    },
                    enabled = !busy,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(if (busy) "Creating..." else "Create account")
                }

                Spacer(Modifier.height(10.dp))

                TextButton(
                    onClick = onBackToLogin,
                    enabled = !busy,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Back to Login")
                }
            }
        }
    }
}

@Composable
private fun AuthBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            )
            .padding(20.dp),
        content = content
    )
}

@Composable
private fun AuthCard(
    header: String,
    subheader: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(MaterialTheme.colorScheme.primary)
        )

        Spacer(Modifier.height(14.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                Text(header, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                Text(subheader, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(18.dp))
                content()
            }
        }
    }
}