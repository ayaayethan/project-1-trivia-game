package com.example.cst438_project_1.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
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
import com.example.cst438_project_1.ui.theme.Cst438project1Theme


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
fun GameScreenPreview() {
    Cst438project1Theme {
        GameScreen()
    }
}