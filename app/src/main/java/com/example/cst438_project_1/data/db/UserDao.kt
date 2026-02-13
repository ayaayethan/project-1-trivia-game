package com.example.cst438_project_1.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun findByUsername(username: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): UserEntity?

    @Update
    suspend fun update(user: UserEntity): Int

    @Query("SELECT bestScore FROM users WHERE id = :id LIMIT 1")
    fun observeBestScore(id: Long): Flow<Int?>

    @Query("""
    UPDATE users 
    SET bestScore = MAX(bestScore, :score)
    WHERE id = :id AND :score > bestScore
    """)
    suspend fun saveBestScoreIfHigher(id: Long, score: Int): Int
}