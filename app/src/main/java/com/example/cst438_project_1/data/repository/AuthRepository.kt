package com.example.cst438_project_1.data.repository

import com.example.cst438_project_1.data.db.UserDao
import com.example.cst438_project_1.data.db.UserEntity
import com.example.cst438_project_1.data.security.PasswordHasher

class AuthRepository(private val userDao: UserDao) {

    suspend fun createAccount(username: String, password: String): Result<Long> {
        val u = username.trim()
        if (u.isBlank()) return Result.failure(Exception("Username required"))
        if (password.length < 6) return Result.failure(Exception("Password must be at least 6 characters"))

        val existing = userDao.findByUsername(u)
        if (existing != null) return Result.failure(Exception("Username already exists"))

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
            ?: return Result.failure(Exception("Invalid username or password"))

        val computed = PasswordHasher.hash(password.toCharArray(), user.salt)
        val ok = PasswordHasher.constantTimeEquals(computed, user.passwordHash)

        return if (ok) Result.success(user)
        else Result.failure(Exception("Invalid username or password"))
    }
}