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
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.cst438_project_1.ui.theme.CyberPurple
import com.example.cst438_project_1.ui.theme.ElectricBlue
import com.example.cst438_project_1.ui.theme.PurpleGrey40
import com.example.cst438_project_1.ui.theme.gametheme


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
    var boxOneColor by remember {mutableStateOf(PurpleGrey40)}
    var boxTwoColor by remember {mutableStateOf(PurpleGrey40)}

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
            if (selectedGame == 0) {
                boxOneColor = androidx.compose.ui.graphics.Color.Red
            } else {
                boxTwoColor = androidx.compose.ui.graphics.Color.Red
            }
        }
    }
    LaunchedEffect(showRatings) {
        if (showRatings && selectedGame != null && !gameOver) {
            if (selectedGame == 0) {
                boxOneColor = androidx.compose.ui.graphics.Color.Green
            } else {
                boxTwoColor = androidx.compose.ui.graphics.Color.Green
            }
            kotlinx.coroutines.delay(1500)
            gamesViewModel.advanceRound(selectedGame!!)
            showRatings = false
            boxOneColor = PurpleGrey40
            boxTwoColor = PurpleGrey40
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

    val retryGame: () -> Unit = {
        gamesViewModel.startGame()
        gameOver = false
        score = 0
        selectedGame = null
        showRatings = false
        boxOneColor = PurpleGrey40
        boxTwoColor = PurpleGrey40
    }

    val guess: (Int) -> Unit = let@{ choice ->
        if (showRatings) return@let // prevent double taps
        selectedGame = choice
        showRatings = true
        val correct = gamesViewModel.evaluateGuess(choice)
        if (correct) {
            score++
        } else {
            gameOver = true
        }
    }
    gametheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = modifier.fillMaxSize()) {


                Text(
                    text = "BEST STREAK: $bestScore",
                    fontWeight = FontWeight.Bold,
                    color = CyberPurple,
                    fontSize = 16.sp,
                    letterSpacing = 4.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                )

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
//                Text("Game Screen Placeholder")
                    Text(
                        text = "SCORE: $score",
                        color = ElectricBlue,
                        fontSize = 24.sp,
                        letterSpacing = 4.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                color = boxOneColor,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )
                    ) {
                        GameCard(
                            game = top!!,
                            showRating = showRatings || top.guessed,
                            onClick = { guess(0) }
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                color = boxTwoColor,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )
                            .padding(2.dp)
                    ) {
                        GameCard(
                            game = bottom!!,
                            showRating = showRatings || bottom.guessed,
                            onClick = { guess(1) }
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(24.dp)
                    ) {
                        Button(
                            onClick = retryGame,
                            modifier = Modifier.alpha(if (gameOver) 1f else 0f),
                            enabled = gameOver
                        ) {
                            Text(
                                text = "RETRY GAME",
                                letterSpacing = 4.sp
                            )
                        }
                    }


                    Button(onClick = onQuitClick) {
                        Text(
                            text = "QUIT",
                            letterSpacing = 4.sp,
//                            fontSize = 24.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GameCard(
    game: Game,
    showRating: Boolean,
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
                    .padding(6.dp),
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
                Spacer(modifier = Modifier.height(60.dp))
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
                                fontSize = 24.sp,
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