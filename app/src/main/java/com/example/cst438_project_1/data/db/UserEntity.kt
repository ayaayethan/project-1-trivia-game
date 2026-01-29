package com.example.cst438_project_1.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["username"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,

    // Stored directly as SQLite BLOB
    val salt: ByteArray,
    val passwordHash: ByteArray,

    val createdAt: Long = System.currentTimeMillis(),
    val highestStreak: Int = 0,
    val currentStreak: Int = 0
)