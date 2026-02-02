package com.example.cst438_project_1.data.repository

import com.example.cst438_project_1.data.db.UserDao
import com.example.cst438_project_1.data.db.UserEntity
import com.example.cst438_project_1.data.security.PasswordHasher

class AuthRepository(private val userDao: UserDao) {

    suspend fun signUp(username: String, password: String): Result<Long> {
        val u = username.trim()
        if (u.isBlank()) return Result.failure(IllegalArgumentException("Username required"))
        if (password.length < 6) return Result.failure(IllegalArgumentException("Password must be at least 6 chars"))

        if (userDao.findByUsername(u) != null) {
            return Result.failure(IllegalStateException("Username already exists"))
        }

        val salt = PasswordHasher.newSalt()
        val hash = PasswordHasher.hash(password.toCharArray(), salt)

        return try {
            val id = userDao.insert(
                UserEntity(
                    username = u,
                    salt = salt,
                    passwordHash = hash
                )
            )
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(username: String, password: String): Result<UserEntity> {
        val u = username.trim()
        val user = userDao.findByUsername(u)
            ?: return Result.failure(IllegalArgumentException("Invalid username or password"))

        val computed = PasswordHasher.hash(password.toCharArray(), user.salt)
        val ok = PasswordHasher.constantTimeEquals(computed, user.passwordHash)

        return if (ok) Result.success(user)
        else Result.failure(IllegalArgumentException("Invalid username or password"))
    }

    suspend fun updateStreak(userId: Long, current: Int, highest: Int): Result<Unit> {
        return try {
            userDao.updateStreaks(userId, current, highest)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}