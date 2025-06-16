package com.github.dragon925.androidlearning.news.ui.viewmodels

import app.cash.turbine.test
import com.github.dragon925.androidlearning.core.api.domain.models.Category
import com.github.dragon925.androidlearning.core.api.domain.repositories.CategoryRepository
import com.github.dragon925.androidlearning.core.api.ui.UIState
import com.github.dragon925.androidlearning.news.ui.models.FilterItem
import com.github.dragon925.androidlearning.news.ui.models.FilterUIState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class FilterViewModelTest {

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    private lateinit var repository: CategoryRepository

    private val category1 = Category("1", "Category1", "url1")
    private val category2 = Category("2", "Category2", "url2")
    private val category3 = Category("3", "Category3", "url3")
    private val rawCategories = listOf(category1, category2, category3)

    private fun createViewModel() = FilterViewModel(repository)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        repository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init - state emits initial (loading false, empty data), then loading, then success with categories`() = runTest {
        coEvery { repository.getCategories() } returns flowOf(rawCategories)

        val expectedData = FilterUIState(rawCategories.map { FilterItem(it, false) })
        val emptyData = FilterUIState(emptyList())

        createViewModel().state.test {
            assertEquals(UIState<FilterUIState, String>(data = emptyData), awaitItem())

            assertEquals(UIState<FilterUIState, String>(isLoading = true, data = emptyData), awaitItem())

            assertEquals(UIState<FilterUIState, String>(data = emptyData), awaitItem())

            assertEquals(UIState<FilterUIState, String>(data = expectedData), awaitItem())

            cancelAndConsumeRemainingEvents()
        }
        coVerify(exactly = 1) { repository.getCategories() }
    }

    @Test
    fun `init - state emits loading then error state (loading true, empty data) when repository throws exception`() = runTest {
        val exception = RuntimeException("Network Error")
        coEvery { repository.getCategories() } returns flow { throw exception }

        val emptyData = FilterUIState(emptyList())

        createViewModel().state.test {
            assertEquals(UIState<FilterUIState, String>(data = emptyData), awaitItem())

            assertEquals(UIState<FilterUIState, String>(isLoading = true, data = emptyData), awaitItem())

            cancelAndConsumeRemainingEvents()
        }
        coVerify(exactly = 1) { repository.getCategories() }
    }

    @Test
    fun `checkCategory - adds category to chosenCategories when isChecked is true`() = runTest {
        coEvery { repository.getCategories() } returns flowOf(rawCategories)

        val expectedData = FilterUIState(listOf(
            FilterItem(category1, true),
            FilterItem(category2, false),
            FilterItem(category3, true)
        ))

        val viewModel = createViewModel()

        viewModel.state.test {
            skipItems(4)

            viewModel.checkCategory(category1.id, category3.id, isChecked = true)

            assertEquals(UIState<FilterUIState, String>(data = expectedData), awaitItem())
            assertEquals(setOf(category1.id, category3.id), viewModel.currentChosenCategories)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `checkCategory - removes category from chosenCategories when isChecked is false`() = runTest {
        coEvery { repository.getCategories() } returns flowOf(rawCategories)

        val expectedData = FilterUIState(listOf(
            FilterItem(category1, true),
            FilterItem(category2, false),
            FilterItem(category3, false)
        ))

        val viewModel = createViewModel()

        viewModel.state.test {
            skipItems(4)

            viewModel.checkCategory(category1.id, category2.id, isChecked = true)
            awaitItem()

            viewModel.checkCategory(category2.id, isChecked = false)

            assertEquals(UIState<FilterUIState, String>(data = expectedData), awaitItem())
            assertEquals(setOf(category1.id), viewModel.currentChosenCategories)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `checkCategory - does not change chosenCategories if already chosen and isChecked is true`() = runTest {
        coEvery { repository.getCategories() } returns flowOf(rawCategories)

        val expectedData = FilterUIState(listOf(
            FilterItem(category1, true),
            FilterItem(category2, false),
            FilterItem(category3, false)
        ))

        val viewModel = createViewModel()

        viewModel.state.test {
            skipItems(4)

            viewModel.checkCategory(category1.id, isChecked = true)
            val stateAfterFirstCheck = awaitItem()
            val chosenAfterFirstCheck = viewModel.currentChosenCategories

            viewModel.checkCategory(category1.id, isChecked = true)
            expectNoEvents()

            assertEquals(stateAfterFirstCheck, UIState<FilterUIState, String>(data = expectedData))
            assertEquals(chosenAfterFirstCheck, viewModel.currentChosenCategories)
            assertEquals(setOf(category1.id), viewModel.currentChosenCategories)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `checkCategory - does not change chosenCategories if not chosen and isChecked is false`() = runTest {
        coEvery { repository.getCategories() } returns flowOf(rawCategories)

        val viewModel = createViewModel()

        viewModel.state.test {
            skipItems(3)

            val initialStateData = awaitItem().data!!.filters
            assertFalse(initialStateData.first { it.category.id == category1.id }.isChecked)
            val initialChosen = viewModel.currentChosenCategories

            viewModel.checkCategory(category1.id, isChecked = false)
            expectNoEvents()

            assertEquals(initialStateData, viewModel.state.first().data?.filters)
            assertEquals(initialChosen, viewModel.currentChosenCategories)
            assertTrue(viewModel.currentChosenCategories.isEmpty())

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `currentChosenCategories - returns the current set of chosen category IDs`() = runTest {
        coEvery { repository.getCategories() } returns flowOf(emptyList())

        val viewModel = createViewModel()
        viewModel.state.test {
            skipItems(3)

            assertTrue(viewModel.currentChosenCategories.isEmpty())

            viewModel.checkCategory("id1", "id2", isChecked = true)
            assertEquals(setOf("id1", "id2"), viewModel.currentChosenCategories)

            viewModel.checkCategory("id1", isChecked = false)
            assertEquals(setOf("id2"), viewModel.currentChosenCategories)

            cancelAndConsumeRemainingEvents()
        }
    }
}