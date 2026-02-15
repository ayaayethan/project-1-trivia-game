package com.example.cst438_project_1.data.repository

import com.example.cst438_project_1.API.DataClass.Game

object GameRepository {
    val queue = ArrayDeque<Game>()
}
