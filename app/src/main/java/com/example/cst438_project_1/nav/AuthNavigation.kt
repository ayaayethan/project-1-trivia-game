package com.example.cst438_project_1.nav

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cst438_project_1.StartGameActivity
import com.example.cst438_project_1.data.db.AppDatabase
import com.example.cst438_project_1.data.repository.AuthRepository
import com.example.cst438_project_1.screens.LoginScreen
import com.example.cst438_project_1.screens.SignUpScreen
import kotlinx.coroutines.launch

private fun onSuccessfulAuth(context: Context) {
    val intent = Intent(context, StartGameActivity::class.java)
    context.startActivity(intent)
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
                            onSuccessfulAuth(context)
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
                                onSuccessfulAuth(context)
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