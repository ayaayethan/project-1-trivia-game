package com.example.cst438_project_1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cst438_project_1.ui.theme.Cst438project1Theme
import com.example.cst438_project_1.viewmodels.GamesViewModel
import kotlin.getValue

class StartGameActivity : ComponentActivity() {

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
                    AppNavigation(onLogout = { onLogout() })
                }
            }
        }
    }

    private fun onLogout() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@Composable
fun AppNavigation(onLogout: () -> Unit) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main_menu") {
        composable("main_menu") {
            MainMenuScreen(
                onPlayClick = { navController.navigate("game_screen") },
                onLogoutClick = onLogout
            )
        }
        composable("game_screen") {
            GameScreen(onQuitClick = { navController.popBackStack() })
        }
    }
}

@Composable
fun MainMenuScreen(
    modifier: Modifier = Modifier,
    onPlayClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val dialogEnabled = remember { mutableStateOf(false) }
    if (dialogEnabled.value) {
        AlertDialog(
            onDismissRequest = {dialogEnabled.value = false},
            title = { Text(text = "Welcome to Game Diff!")},
            text = {
                Text(text = "Select which game you think is rated higher! " +
                        "If your guess is correct, you will move on to the next round and receive a point.\n" +
                        "\nTry to pump your streak as high as possible!")
                   },
            confirmButton = {
                Button(onClick = {dialogEnabled.value = false})
                {Text(text = "Exit")}
            }
        )
    }
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Game Diff",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "pick which game you think is rated higher",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onPlayClick) {
                Text(text = "Play")
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onLogoutClick) {
                Text(text = "Logout")
            }
            Button(onClick = {dialogEnabled.value = true}) {
                Text(text = "Help")
            }
        }
    }
}

@Composable
fun GameScreen(modifier: Modifier = Modifier, onQuitClick: () -> Unit = {}) {
    var score by remember { mutableIntStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }


    val updateScore: () -> Unit = {
        if(!gameOver) {
            score++
        }
    }

    val wrongAnswer: () -> Unit = {
        gameOver = true
    }

    val retryGame: () -> Unit = {
        gameOver = false
        score = 0
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Game Screen Placeholder")
        Text("Score: $score")
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = updateScore,
            enabled = !gameOver
        ) {
            Text(text = "Update Score")
        }
        Button(
            onClick = wrongAnswer,
            enabled = !gameOver
        ) {
            Text(text = "Wrong Answer")
        }
        Button(
            onClick = retryGame,
            modifier = Modifier.alpha(if (gameOver) 1f else 0f),
            enabled = gameOver
        ) {
            Text(text = "Retry Game")
        }

        Button(onClick = onQuitClick) {
            Text(text = "Quit")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainMenuScreenPreview() {
    Cst438project1Theme {
        MainMenuScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    Cst438project1Theme {
        GameScreen()
    }
}
