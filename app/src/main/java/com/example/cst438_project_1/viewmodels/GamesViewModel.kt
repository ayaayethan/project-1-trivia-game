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

class GamesViewModel : ViewModel() {
    private val _games = MutableLiveData<List<Game>>()
    val games : LiveData<ArrayDeque<Game>> = _games

    // Loads 10 games into the queue
    private fun fetchGames() {
        viewModelScope.launch {
            try {
                // generates a random page from APIs
                val page : Int = Random.nextInt(1,465)
                val apiKey : String = BuildConfig.API_KEY

                val response = RetrofitInstance.api.getGames(
                    key = apiKey,
                    page = page, // random integer for page sizes
                    page_size = 10,
                    metacritic = "70,100"
                )

                if (response.isSuccessful) {
                    // TODO: Shuffle games inside the response
                    _games.value = response.body()?.results ?: emptyList()
                } else {
                    Log.e("API", "Error: ${response.code()}")
                }

            } catch (e: Exception){
                Log.e("API: ", "Exception: ${e.message}")
            }
        }
    }

    // Simple function to log games from the API
    fun debugFetchGames() {
        viewModelScope.launch(Dispatchers.IO) {
            val apiKey : String = BuildConfig.API_KEY

            val response = RetrofitInstance.api.getGames(
                key = apiKey,
                page = 1,
                page_size = 10,
                metacritic = "70,100"
            )

            Log.d("API DEBUG", response.body()?.results.toString())
        }
    }

    // Pulls two games from the front of the queue and returns them
    fun stageGames() {
        // if list of games is less than or equal to 2, fetch 10 more games

        // dequeue front 2 games from queue and set a public stage variable containing two games
    }

    // Pass in the incorrect gameId, swap it with a new one. Updates stage
    fun swapGame() {

    }




}