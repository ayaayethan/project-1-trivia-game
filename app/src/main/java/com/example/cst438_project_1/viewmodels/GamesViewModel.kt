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

class GamesViewModel : ViewModel() {
    private val _games = MutableLiveData<List<Game>>()
    val games : LiveData<List<Game>> = _games

    fun fetchGames() {
        viewModelScope.launch {
            try {
                val page : Int = Random.nextInt(1,35001)
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
}