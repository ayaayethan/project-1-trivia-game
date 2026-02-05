@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.example.cst438_project_1

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.cst438_project_1.API.ApiInterface
import com.example.cst438_project_1.API.DataClass.Game
import com.example.cst438_project_1.API.DataClass.GameList
import com.example.cst438_project_1.API.RetrofitInstance
import com.example.cst438_project_1.viewmodels.GamesViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import kotlin.random.Random

class GamesViewModelTest {

    // makes LiveData work in unit tests
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // fakes the Android main thread
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mockApi: ApiInterface

    @Before
    fun setUp() {
        mockkObject(RetrofitInstance)
        mockApi = mockk()
        every { RetrofitInstance.api } returns mockApi
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `fetchGames gets 10 games`() = runTest {
        val games = List(10) { mockk<Game>() }

        coEvery {
            mockApi.getGames(
                key = any(),
                page = any(),
                page_size = 10,
                metacritic = any()
            )
        } returns Response.success(GameList(results = games))

        val viewmodel = GamesViewModel()
        viewmodel.fetchGames()
        advanceUntilIdle()

        assertEquals(10, viewmodel.games.value?.size)
    }

    @Test
    fun `fetchGames contains the appropriate fields`() = runTest {
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
                metacritic = any()
            )
        } returns Response.success(GameList(results = listOf(game)))

        val viewmodel = GamesViewModel()
        viewmodel.fetchGames()
        advanceUntilIdle()

        val first = viewmodel.games.value!!.first()

        assertEquals(123, first.id)
        assertEquals("Expedition 33", first.name)
        assertEquals("2001-11-15", first.released)
        assertEquals(97, first.metacritic)
        assertEquals("https://example.com/image.jpg", first.background_image)
    }
}