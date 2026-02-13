package com.example.cst438_project_1.API.DataClass

data class Game(
    val background_image: String,
    val id: Int,
    val metacritic: Int,
    val name: String,
    val released: String,
    val guessed: Boolean = false
)