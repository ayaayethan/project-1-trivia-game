package com.example.cst438_project_1.dbtests

import com.example.cst438_project_1.data.db.AppDatabase
import com.example.cst438_project_1.data.db.UserDao
import com.example.cst438_project_1.data.db.UserEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserDaoHighScoreTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: UserDao

    @Before
    fun setup() {
        db = TestDbFactory.inMemoryDb()
        dao = db.userDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    private suspend fun insertUser(username: String = "user", best: Int = 0): Long {
        return dao.insert(
            UserEntity(
                username = username,
                salt = byteArrayOf(1, 2, 3),
                passwordHash = byteArrayOf(4, 5, 6),
                bestScore = best
            )
        )
    }

    private suspend fun best(id: Long): Int {
        return dao.findById(id)?.bestScore ?: error("User not found for id=$id")
    }

    @Test
    fun initialBestScoreIsZero() = runTest {
        val id = insertUser(best = 0)
        assertEquals(0, best(id))
    }

    @Test
    fun savingHigherScoreOverwritesBestScore() = runTest {
        val id = insertUser(best = 2)

        dao.saveBestScoreIfHigher(id, 6)

        assertEquals(6, best(id))
    }

    @Test
    fun savingLowerScoreDoesNotOverwriteBestScore() = runTest {
        val id = insertUser(best = 6)

        dao.saveBestScoreIfHigher(id, 3)

        assertEquals(6, best(id))
    }

    @Test
    fun savingEqualScoreDoesNotOverwriteBestScore() = runTest {
        val id = insertUser(best = 6)

        dao.saveBestScoreIfHigher(id, 6)

        assertEquals(6, best(id))
    }

    @Test
    fun multipleSavesEndsWithMaxScore() = runTest {
        val id = insertUser(best = 0)

        dao.saveBestScoreIfHigher(id, 1)
        dao.saveBestScoreIfHigher(id, 5)
        dao.saveBestScoreIfHigher(id, 2)
        dao.saveBestScoreIfHigher(id, 9)
        dao.saveBestScoreIfHigher(id, 7)

        assertEquals(9, best(id))
    }

    @Test
    fun savingScoreForOneUserDoesNotAffectAnother() = runTest {
        val id1 = insertUser(username = "user1", best = 4)
        val id2 = insertUser(username = "user2", best = 8)

        dao.saveBestScoreIfHigher(id1, 10) // becomes 10
        dao.saveBestScoreIfHigher(id2, 3)  // stays 8

        assertEquals(10, best(id1))
        assertEquals(8, best(id2))
    }
}