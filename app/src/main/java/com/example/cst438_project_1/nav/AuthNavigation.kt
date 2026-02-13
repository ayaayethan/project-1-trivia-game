package com.example.cst438_project_1.nav

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cst438_project_1.StartGameActivity
import com.example.cst438_project_1.data.db.AppDatabase
import com.example.cst438_project_1.data.repository.AuthRepository
import com.example.cst438_project_1.screens.LoginScreen
import com.example.cst438_project_1.screens.SignUpScreen
import com.example.cst438_project_1.ui.theme.gametheme // Matches your filename
import kotlinx.coroutines.launch

private fun onSuccessfulAuth(context: Context) {
    val intent = Intent(context, StartGameActivity::class.java).apply {
        // Clear activity stack so user can't "back" into login after successful auth
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        putExtra(StartGameActivity.EXTRA_USER_ID, userId)
    }
    context.startActivity(intent)
}

@Composable
fun AuthNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Initialize Database and Repository
    val db = remember { AppDatabase.get(context) }
    val authRepo = remember { AuthRepository(db.userDao()) }

    var loginError by remember { mutableStateOf<String?>(null) }
    var signUpError by remember { mutableStateOf<String?>(null) }

    gametheme {
        NavHost(
            navController = navController,
            startDestination = "login"
        ) {
            composable("login") {
                LoginScreen(
                    errorMessage = loginError,
                    onLoginClick = { username, password ->
                        loginError = null
                        scope.launch {
                            val res = authRepo.login(username, password)
                            if (res.isSuccess) {
                                onSuccessfulAuth(context, res.getOrThrow().id)
                            } else {
                                loginError = res.exceptionOrNull()?.message ?: "Login failed"
                            }
                        }
                    },
                    onSignUpClick = {
                        loginError = null
                        navController.navigate("signup")
                    }
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
                                    onSuccessfulAuth(context, res.getOrThrow())
                                } else {
                                    signUpError = res.exceptionOrNull()?.message ?: "Sign up failed"
                                }
                            }
                        }
                    },
                    onBackClick = {
                        signUpError = null
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}