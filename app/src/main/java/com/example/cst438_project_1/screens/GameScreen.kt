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
import androidx.compose.ui.platform.LocalContext
import com.example.cst438_project_1.data.db.AppDatabase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.filterNotNull
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme


@Composable
fun GameScreen(
    userId: Long,
    modifier: Modifier = Modifier,
    onQuitClick: () -> Unit = {},
    gamesViewModel: GamesViewModel = viewModel()
) {
    var score by remember { mutableIntStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val userDao = remember { db.userDao() }
    var selectedGame by remember { mutableStateOf<Int?>(null) }
    var showRatings by remember { mutableStateOf(false) }
    var previousChoice by remember { mutableStateOf<Int?>(null) }


    val bestScore by userDao
        .observeBestScore(userId)
        .map { it ?: 0 }
        .collectAsState(initial = 0)



    // observer for stage. will update automatically when a game is swapped
    val stage by gamesViewModel.stage.observeAsState()

    LaunchedEffect(Unit) {
        gamesViewModel.startGame()
    }
    LaunchedEffect(gameOver) {
        if (gameOver) {
            db.userDao().saveBestScoreIfHigher(userId, score)
        }
    }
    LaunchedEffect(showRatings) {
        if (showRatings && previousChoice != null && !gameOver) {
                kotlinx.coroutines.delay(1500)
                gamesViewModel.advanceRound(previousChoice!!)
                showRatings = false
                selectedGame = null
            }
    }

    if (stage == null || stage!!.top == null || stage!!.bot == null ) {
        Text(
            text ="Loading...",
            modifier = Modifier
                .padding(16.dp),
        )
        return
    }

    val top = stage!!.top // top game
    val bottom = stage!!.bot // bottom game

//    val updateScore: () -> Unit = {
//        if(!gameOver) {
//            score++
//        }
//    }
//
//    val wrongAnswer: () -> Unit = {
//        gameOver = true
//    }

    val retryGame: () -> Unit = {
        gamesViewModel.startGame()
        gameOver = false
        score = 0
        selectedGame = null
        showRatings = false
    }

    val guess: (Int) -> Unit = let@{ choice ->
        if (showRatings) return@let // prevent double taps
        previousChoice = choice
        selectedGame = choice
        showRatings = true
        val correct = gamesViewModel.evaluateGuess(choice)
        if (correct) {
            score++
        } else {
            gameOver = true
        }
    }
    Box(modifier = modifier.fillMaxSize()) {


        Text(
            text = "Best Streak: $bestScore",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Game Screen Placeholder")
            Text("Score: $score")
            Spacer(modifier = Modifier.height(24.dp))

            GameCard(
                game = top!!,
                showRating = showRatings || top.guessed,
                isSelected = selectedGame == 0,
                onClick = { guess(0) }
            )
            GameCard(
                game = bottom!!,
                showRating = showRatings || bottom.guessed,
                isSelected = selectedGame == 1,
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
}

@Composable
fun GameCard(
    game: Game,
    showRating: Boolean,
    isSelected: Boolean,
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
        Box {
            // Game image
            coil.compose.AsyncImage(
                model = game.background_image,
                contentDescription = game.name,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box( // Game Title
                    modifier = Modifier
                        .background(
                            androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.5f),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = game.name,
                        color = androidx.compose.ui.graphics.Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                if (showRating || game.seen) {
                        Box( // Game Rating
                            modifier = Modifier
                                .background(
                                    androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.5f),
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "${game.metacritic}/100",
                                color = androidx.compose.ui.graphics.Color.White,
                                modifier = Modifier
                            )
                        }
                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    Cst438project1Theme {
        GameScreen(userId = 1L)
    }
}