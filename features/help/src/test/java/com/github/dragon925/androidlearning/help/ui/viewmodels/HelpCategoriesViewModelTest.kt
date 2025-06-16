package com.github.dragon925.androidlearning.help.ui.viewmodels

import app.cash.turbine.test
import com.github.dragon925.androidlearning.core.api.domain.models.Category
import com.github.dragon925.androidlearning.core.api.domain.repositories.CategoryRepository
import com.github.dragon925.androidlearning.core.api.ui.UIState
import com.github.dragon925.androidlearning.help.ui.models.HelpCategoryUIState
import com.github.dragon925.androidlearning.help.ui.utils.toHelpCategoryItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HelpCategoriesViewModelTest {

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    private lateinit var repository: CategoryRepository
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        repository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = HelpCategoriesViewModel(repository)

    @Test
    fun `init - emits initial, loading, then success state with categories`() = runTest(testScheduler) {
        val rawCategories = listOf(
            Category("1", "Category1", "url1"),
            Category("2", "Category2", "url2"),
            Category("3", "Category3", "url3"),
        )
        coEvery { repository.getCategories() } returns flowOf(rawCategories)
        val expectedState = HelpCategoryUIState(rawCategories.map { it.toHelpCategoryItem() })

        createViewModel().state.test {
            assertEquals(UIState<HelpCategoryUIState, String>(), awaitItem())
            assertEquals(UIState<HelpCategoryUIState, String>(isLoading = true), awaitItem())
            assertEquals(UIState<HelpCategoryUIState, String>(), awaitItem())
            assertEquals(UIState<HelpCategoryUIState, String>(data = expectedState), awaitItem())
            cancelAndConsumeRemainingEvents()
        }

        coVerify(exactly = 1) { repository.getCategories() }
    }

    @Test
    fun `init - emits initial, loading, then error state (null data) when repository throws exception`() = runTest {
        val exception = RuntimeException("Network Error")
        coEvery { repository.getCategories() } returns flow { throw exception }

        createViewModel().state.test {
            assertEquals(UIState<HelpCategoryUIState, String>(), awaitItem())

            assertEquals(UIState<HelpCategoryUIState, String>(isLoading = true), awaitItem())

            cancelAndConsumeRemainingEvents()
        }

        coVerify(exactly = 1) { repository.getCategories() }
    }

    @Test
    fun `state flow - first emitted state is loading false and data null before loading starts`() = runTest {
        coEvery { repository.getCategories() } returns flowOf(emptyList())

        createViewModel().state.test {
            assertEquals(UIState<HelpCategoryUIState, String>(), awaitItem())
            cancelAndConsumeRemainingEvents()
        }

    }

    @Test
    fun `loadCategories - correctly maps categories to HelpCategoryUIState`() = runTest {
        val rawCategories = listOf(
            Category("1", "Category1", "url1"),
            Category("2", "Category2", "url2"),
            Category("3", "Category3", "url3"),
        )

        val expectedState = HelpCategoryUIState(rawCategories.map { it.toHelpCategoryItem() })

        coEvery { repository.getCategories() } returns flowOf(rawCategories)

        createViewModel().state.test {
            repeat(3) { awaitItem() }
            assertEquals(expectedState, awaitItem().data)
            cancelAndConsumeRemainingEvents()
        }
    }
}