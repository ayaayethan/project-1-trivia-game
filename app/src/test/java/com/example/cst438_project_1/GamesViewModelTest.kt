@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.example.cst438_project_1

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.example.cst438_project_1.API.ApiInterface
import com.example.cst438_project_1.API.DataClass.Game
import com.example.cst438_project_1.API.DataClass.GameList
import com.example.cst438_project_1.API.RetrofitInstance
import com.example.cst438_project_1.data.repository.GameRepository
import com.example.cst438_project_1.viewmodels.GamesViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Response

@RunWith(RobolectricTestRunner::class)
class GamesViewModelTest {

    // makes LiveData work in unit tests
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // fakes the Android main thread
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mockApi: ApiInterface
    private lateinit var app: Application

    @Before
    fun setUp() {
        app = ApplicationProvider.getApplicationContext()

        // important: clear shared queue between tests
        GameRepository.queue.clear()

        mockkObject(RetrofitInstance)
        mockApi = mockk()
        every { RetrofitInstance.api } returns mockApi
    }

    @After
    fun tearDown() {
        GameRepository.queue.clear()
        unmockkAll()
    }

    @Test
    fun `preloadInitialGames loads 10 games into repository queue`() = runTest {
        val games = List(10) { i ->
            Game(
                id = i + 1,
                name = "Game ${i + 1}",
                released = "2001-11-15",
                metacritic = 70 + i,
                background_image = "https://example.com/image${i + 1}.jpg"
            )
        }

        coEvery {
            mockApi.getGames(
                key = any(),
                page = any(),
                page_size = 10,
                metacritic = "70,100",
                platforms = "4,187,1,18,186,7,14,16"
            )
        } returns Response.success(GameList(results = games))

        val viewModel = GamesViewModel(app)

        viewModel.preloadInitialGames()
        advanceUntilIdle()

        assertEquals(10, GameRepository.queue.size)
    }

    @Test
    fun `preloadInitialGames contains the appropriate fields`() = runTest {
        val game = Game(
            id = 123,
            name = "Expedition 33",
            released = "2001-11-15",
            metacritic = 97,
            background_image = "https://example.com/image.jpg"
        )

        coEvery {
            mockApi.getGames(
                key = any(),
                page = any(),
                page_size = 10,
                metacritic = "70,100",
                platforms = "4,187,1,18,186,7,14,16"
            )
        } returns Response.success(GameList(results = listOf(game)))

        val viewModel = GamesViewModel(app)

        viewModel.preloadInitialGames()
        advanceUntilIdle()

        val first = GameRepository.queue.first()

        assertEquals(123, first.id)
        assertEquals("Expedition 33", first.name)
        assertEquals("2001-11-15", first.released)
        assertEquals(97, first.metacritic)
        assertEquals("https://example.com/image.jpg", first.background_image)

        // new fields default false
        assertEquals(false, first.guessed)
        assertEquals(false, first.seen)
    }

    @Test
    fun `evaluateGuess returns correct boolean based on metacritic score`() = runTest {
        GameRepository.queue.clear()

        val top = Game(
            id = 1,
            name = "Expedition 33",
            released = "2001-11-15",
            metacritic = 97,
            background_image = "https://example.com/image.jpg"
        )

        val bot = Game(
            id = 2,
            name = "Cyberpunk 2077",
            released = "2001-11-15",
            metacritic = 95,
            background_image = "https://example.com/image.jpg"
        )

        GameRepository.queue.add(top)
        GameRepository.queue.add(bot)

        val viewModel = GamesViewModel(app)

        viewModel.startGame()
        advanceUntilIdle()

        // choice 0 means user picked top, which is correct
        assertEquals(true, viewModel.evaluateGuess(0))
        // choice 1 means user picked bottom, which is incorrect
        assertEquals(false, viewModel.evaluateGuess(1))
    }

    @Test
    fun `advanceRound swaps out the lower rated game`() = runTest {
        GameRepository.queue.clear()

        val game1 = Game("img", 1, 97, "Expedition 33", "2001-11-15")
        val game2 = Game("img", 2, 95, "Cyberpunk 2077", "2001-11-15")
        val game3 = Game("img", 3, 80, "Minecraft", "2001-11-15")
        val game4 = Game("img", 4, 90, "Elden Ring", "2001-11-15")

        // startGame takes first two
        // remaining queue after start: [game3, game4]
        GameRepository.queue.addAll(listOf(game1, game2, game3, game4))

        val viewModel = GamesViewModel(app)

        viewModel.startGame()
        advanceUntilIdle()

        // Test initial stage
        assertEquals(1, viewModel.stage.value!!.top!!.id)
        assertEquals(2, viewModel.stage.value!!.bot!!.id)

        // Choose top game as guessed
        viewModel.advanceRound(0)
        advanceUntilIdle()

        // bottom should now be game3, top stays game1
        assertEquals(1, viewModel.stage.value!!.top!!.id)
        assertEquals(3, viewModel.stage.value!!.bot!!.id)
        assertTrue(game1.guessed) // assert that game1 guessed is true

        // choose bottom as guessed
        viewModel.advanceRound(1)
        advanceUntilIdle()

        // top should now be game4, bottom stays game3
        assertEquals(4, viewModel.stage.value!!.top!!.id)
        assertEquals(3, viewModel.stage.value!!.bot!!.id)
        assertTrue(game3.guessed)
    }

    @Test
    fun `advanceRound swaps out the correct game if it was already guessed`() = runTest {
        GameRepository.queue.clear()

        val game1 = Game("img", 1, 97, "Expedition 33", "2001-11-15")
        val game2 = Game("img", 2, 95, "Cyberpunk 2077", "2001-11-15")
        val game3 = Game("img", 3, 80, "Minecraft", "2001-11-15")
        val game4 = Game("img", 4, 90, "Elden Ring", "2001-11-15")

        // startGame takes first two
        // remaining queue after start: [game3, game4]
        GameRepository.queue.addAll(listOf(game1, game2, game3, game4))

        val viewModel = GamesViewModel(app)

        viewModel.startGame()
        advanceUntilIdle()

        // Test initial stage
        assertEquals(1, viewModel.stage.value!!.top!!.id)
        assertEquals(2, viewModel.stage.value!!.bot!!.id)

        // Choose top game as guessed
        viewModel.advanceRound(0)
        advanceUntilIdle()

        // bottom should now be game3, top stays game1
        assertEquals(1, viewModel.stage.value!!.top!!.id)
        assertEquals(3, viewModel.stage.value!!.bot!!.id)
        assertTrue(game1.guessed) // assert that game1 guessed is true

        // choose top as guessed again
        viewModel.advanceRound(0)
        advanceUntilIdle()

        // top should now be game4, bottom stays game3
        assertEquals(4, viewModel.stage.value!!.top!!.id)
        assertEquals(3, viewModel.stage.value!!.bot!!.id)
    }
}
