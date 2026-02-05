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
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import kotlin.random.Random

class GamesViewModelTest {

    // Makes LiveData work in unit tests
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Fakes the Android main thread
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `fetchGames gets 10 games`() = runTest {
        mockkObject(RetrofitInstance)

        val mockApi = mockk<ApiInterface>()
            every { RetrofitInstance.api } returns mockApi

        val tenGames = List(10) { mockk<Game>() }

        val fakeResponse = GameList (
            results = tenGames
        )

        coEvery {
            mockApi.getGames(
                key = any(),
                page = any(),
                page_size = 10,
                metacritic = any()
            )
        } returns Response.success(fakeResponse)

        val viewModel = GamesViewModel()

        viewModel.fetchGames()
        advanceUntilIdle()

        assertEquals(10, viewModel.games.value?.size)
    }
}