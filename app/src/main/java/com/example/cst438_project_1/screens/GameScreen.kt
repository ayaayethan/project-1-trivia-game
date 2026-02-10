package com.example.cst438_project_1.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cst438_project_1.API.DataClass.Game
import com.example.cst438_project_1.ui.theme.Cst438project1Theme
import com.example.cst438_project_1.viewmodels.GamesViewModel


@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    onQuitClick: () -> Unit = {},
    gamesViewModel: GamesViewModel = viewModel()
) {
    var score by remember { mutableIntStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }

    // observer for stage. will update automatically when a game is swapped
    val stage by gamesViewModel.stage.observeAsState()

    LaunchedEffect(Unit) {
        gamesViewModel.startGame()
    }

    if (stage == null) {
        Text("Loading...")
        return
    }

    val top = stage!!.top // top game
    val bottom = stage!!.bot // bottom game

    val updateScore: () -> Unit = {
        if(!gameOver) {
            score++
        }
    }

    val wrongAnswer: () -> Unit = {
        gameOver = true
    }

    val retryGame: () -> Unit = {
        gamesViewModel.startGame()
        gameOver = false
        score = 0
    }

    val guess: (Int) -> Unit = { choice ->
        if (gamesViewModel.makeGuess(choice)) {
            score++
        } else {
            gameOver = true
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Game Screen Placeholder")
        Text("Score: $score")
        Spacer(modifier = Modifier.height(24.dp))

        GameCard(
            game = top,
            onClick = { guess(0) }
        )
        GameCard(
            game = bottom,
            onClick = { guess(1) }
        )

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

@Composable
fun GameCard(
    game: Game,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .padding(8.dp)
            .clickable { onClick() },
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(8.dp)
    ) {
        androidx.compose.foundation.layout.Box {
            // Game image
            coil.compose.AsyncImage(
                model = game.background_image,
                contentDescription = game.name,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Game title
            androidx.compose.material3.Text(
                text = game.name,
                color = androidx.compose.ui.graphics.Color.White,
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(androidx.compose.ui.Alignment.Center)
                    .padding(16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
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