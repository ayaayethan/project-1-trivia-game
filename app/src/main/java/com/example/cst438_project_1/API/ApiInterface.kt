package com.example.cst438_project_1.API

import com.example.cst438_project_1.API.DataClass.Game
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("/games")
    suspend fun getGames(@Query("key") key : String): Response<List<Game>>
}