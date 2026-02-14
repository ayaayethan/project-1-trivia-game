package com.example.cst438_project_1.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cst438_project_1.API.DataClass.Game
import com.example.cst438_project_1.API.RetrofitInstance
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt
import com.example.cst438_project_1.BuildConfig
import kotlinx.coroutines.Dispatchers

data class Stage(val top: Game?, val bot: Game?)
class GamesViewModel : ViewModel() {
    private val queue = ArrayDeque<Game>()
    private val _stage = MutableLiveData<Stage>()
    val stage: LiveData<Stage> = _stage

    /**
     * Loads 10 games into the queue
     */
    private suspend fun fetchGames() {
        try {
            // generates a random page from APIs
            val page : Int = Random.nextInt(1,101)
            val apiKey : String = BuildConfig.API_KEY

            val response = RetrofitInstance.api.getGames(
                key = apiKey,
                page = page, // random integer for page sizes
                page_size = 10,
                metacritic = "70,100",
                platforms = "4,187,1,18,186,7,14,16"
            )

            if (response.isSuccessful) {
                // TODO: Shuffle games inside the response
                val gameList = response.body()?.results ?: emptyList()
                queue.addAll(gameList);
            } else {
                Log.e("API", "Error: ${response.code()}")
            }

        } catch (e: Exception) {
            Log.e("API: ", "Exception: ${e.message}")
        }
    }

    /**
     * Simple function to log games from the API
     */
    fun debugFetchGames() {
        viewModelScope.launch(Dispatchers.IO) {
            val apiKey : String = BuildConfig.API_KEY

            val response = RetrofitInstance.api.getGames(
                key = apiKey,
                page = 1,
                page_size = 10,
                metacritic = "70,100",
                platforms = "4,187,1,18,186,7,14,16"
            )

            Log.d("API DEBUG", response.body()?.results.toString())
        }
    }

    /**
     * Initializes stage with two random games.
     */
    fun startGame() {
        viewModelScope.launch {
            if (queue.size <= 2) {
                fetchGames();
            }

            _stage.value = Stage(
                queue.removeFirst(),
                queue.removeFirst()
            );
        }
    }

//    /**
//     * Determines whether the player's guess is correct.
//     *
//     * @param choice `0` if user selected top game. `1` if user selected bottom game.
//     * @return `true` if the player's guess was correct and swaps out lower game, `false` otherwise
//     */
//    fun makeGuess(choice: Int): Boolean {
//        val stage = _stage.value ?: return false // return if _stage is not initialized
//
//        if (choice == 0) { // user chose top game
//            if (stage.top!!.metacritic >= stage.bot!!.metacritic) {
//                // if top game has been guessed already, swap it
//                if (stage.top.guessed) {
//                    swapGame(0);
//                } else { // else swap bottom game
//                    swapGame(1);
//                    markAsGuessed(0); // mark top game as guessed
//                }
//                return true;
//            } else {
//                return false;
//            }
//        } else { // user chose bottom game
//            if (stage.bot!!.metacritic >= stage.top!!.metacritic) {
//                // if bottom game has already been guessed, swap the bottom game
//                if (stage.bot.guessed) {
//                    swapGame(1);
//                } else { // otherwise, swap top game
//                    swapGame(0);
//                    markAsGuessed(1); // mark bottom game as guessed
//                }
//                return true;
//            } else {
//                return false;
//            }
//        }
//    }
    /**
     * Determines whether the player's guess is correct.
     *
     * @param choice `0` if user selected top game. `1` if user selected bottom game.
     * @return `true` if the player's guess was correct, `false` otherwise
     */
    fun evaluateGuess(choice: Int): Boolean {
        val stage = _stage.value ?: return false

        return if (choice == 0) {
            stage.top!!.metacritic >= stage.bot!!.metacritic
        } else {
            stage.bot!!.metacritic >= stage.top!!.metacritic
        }
    }

    /**
     * Advances the game to the next round by swapping either the top, bottom, or both games if
     * selected game was seen in the previous round.
     *
     * @param choice '0' for top game, '1' for bottom game
     */
    fun advanceRound(choice: Int) {
        val stage = _stage.value ?: return
        val top = stage.top ?: return
        val bot = stage.bot ?: return
        _stage.value = stage.copy(
                top = top.copy(seen = true),
                bot = bot.copy(seen = true)
            )
        if (choice == 0) {
            if (!stage.top!!.guessed) {
                markAsGuessed(0)
                swapGame(1)
            } else {
                swapGame(0)
            }
        } else {
            if (!stage.bot!!.guessed) {
                markAsGuessed(1)
                swapGame(0)
            } else {
                swapGame(1)
            }
        }
    }

    /**
     * Swaps out the game with the lower rating from the stage
     *
     * @param gameToSwap the game we need to swap
     */
    private fun swapGame(gameToSwap: Int) {
        viewModelScope.launch {
            val stage = _stage.value ?: return@launch;

            val  newStage = if (gameToSwap == 0) { // swap the top game
                Stage(queue.removeFirstOrNull(), stage.bot);
            } else { // swap the bottom game
                Stage(stage.top, queue.removeFirstOrNull());
            }

            _stage.value = newStage;

            // reload queue if it's running low
            if (queue.size <= 2) {
                fetchGames();
            }
        }
    }

    /**
     * Marks a game as guessed
     *
     * @param gameToMark Game to mark as guessed. `0` for top `1` for bottom
     */
    private fun markAsGuessed(gameToMark: Int) {
        val currentStage = _stage.value ?: return

        _stage.value = when (gameToMark) {
            0 -> {
                val top = currentStage.top ?: return
                currentStage.copy(top = top.copy(guessed = true))
            }
            1 -> {
                val bot = currentStage.bot ?: return
                currentStage.copy(bot = bot.copy(guessed = true))
            }
            else -> return
        }
    }
}