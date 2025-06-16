package com.github.dragon925.androidlearning.news.ui.viewmodels

import app.cash.turbine.test
import com.github.dragon925.androidlearning.core.api.domain.models.Event
import com.github.dragon925.androidlearning.core.api.domain.models.Mapper
import com.github.dragon925.androidlearning.core.api.domain.repositories.EventRepository
import com.github.dragon925.androidlearning.core.api.ui.UIState
import com.github.dragon925.androidlearning.news.ui.models.NewsItem
import com.github.dragon925.androidlearning.news.ui.models.NewsListUIState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class NewsViewModelTest {

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    private lateinit var eventsFlow: MutableStateFlow<List<Event>>
    private lateinit var readIdsFlow: MutableStateFlow<Set<String>>

    private lateinit var repository: EventRepository
    private lateinit var mapper: Mapper<Event, NewsItem>

    private val eventsCategories = listOf(listOf("1", "2"), listOf("2", "3"), listOf("1"))
    private val rawNews = List(3) { i ->
        val id = i + 1
        Event(
            id = "$id",
            name = "Event$id",
            description = "Event$id description",
            organizer = "Organizer$id",
            categoryIds = eventsCategories[i],
            address = "Address$id",
            phoneNumbers = listOf("Phone$id"),
            email = "email$id",
            website = "website$id",
            photos = listOf("url$id"),
            startDate = LocalDate(2000 + id, id, id),
            endDate = LocalDate(2000 + id, id, id)
        )
    }
    private val mappedNews = List(3) { i ->
        val id = i + 1
        NewsItem(
            id = "$id",
            title = "Event$id",
            description = "Event$id description",
            date = "0${id}/0${id}/200${id}",
            categoryIds = eventsCategories[i].toImmutableSet(),
            image = "url$id"
        )
    }

    private fun createViewModel() = NewsViewModel(repository, mapper)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        repository = mockk(relaxed = true)
        mapper = mockk()

        eventsFlow = MutableStateFlow(emptyList())
        readIdsFlow = MutableStateFlow(emptySet())

        coEvery { repository.getEvents() } returns eventsFlow
        coEvery { repository.getReadEventIds() } returns readIdsFlow

        every { mapper.invoke(rawNews[0]) } returns mappedNews[0]
        every { mapper.invoke(rawNews[1]) } returns mappedNews[1]
        every { mapper.invoke(rawNews[2]) } returns mappedNews[2]
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init - state emits initial, then loading, then success with news and read IDs`() = runTest {
        val emptyData = NewsListUIState()

        createViewModel().state.test {
            // 1. Начальное состояние
            assertEquals(UIState<NewsListUIState, String>(data = emptyData), awaitItem())

            // 2. Состояние загрузки
            assertEquals(UIState<NewsListUIState, String>(isLoading = true, data = emptyData), awaitItem())

            // 3. Состояние окончания загрузки
            assertEquals(UIState<NewsListUIState, String>(data = emptyData), awaitItem())

            // 4. Эмитируем события и прочитанные ID
            eventsFlow.value = rawNews

            // 4. Состояние успеха
            val expectedNews = mappedNews.toImmutableList()

            assertEquals(
                UIState<NewsListUIState, String>(data = NewsListUIState(expectedNews)),
                awaitItem()
            )

            readIdsFlow.value = setOf("1")
            val expectedReadIds = setOf("1").toImmutableSet()

            assertEquals(
                UIState<NewsListUIState, String>(data = NewsListUIState(expectedNews, expectedReadIds)),
                awaitItem()
            )

            cancelAndConsumeRemainingEvents()
        }

        coVerify { repository.getEvents() }
        coVerify { repository.getReadEventIds() }
        coVerify { mapper.invoke(rawNews[0]) }
        coVerify { mapper.invoke(rawNews[1]) }
    }

    @Test
    fun `init - error loading news - state reflects loading true and last known data`() = runTest {
        val exception = RuntimeException("Network Error News")
        coEvery { repository.getEvents() } returns flow { throw exception }
        coEvery { repository.getReadEventIds() } returns flowOf(setOf("1"))


        createViewModel().state.test {
            assertEquals(UIState<NewsListUIState, String>(data = NewsListUIState()), awaitItem())
            skipItems(1)
            assertEquals(UIState<NewsListUIState, String>(
                isLoading = true,
                data = NewsListUIState(readIds = setOf("1").toImmutableSet())), awaitItem()
            )

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `setFilters - updates state with filtered news`() = runTest {
        coEvery { repository.getEvents() } returns flowOf(rawNews)
        coEvery { repository.getReadEventIds() } returns flowOf(emptySet())

        val viewModel = createViewModel()

        viewModel.state.test {
            skipItems(4)

            viewModel.setFilters(listOf("1"))

            val expectedFilteredNews = listOf(mappedNews[0], mappedNews[2]).toImmutableList()
            assertEquals(
                UIState<NewsListUIState, String>(data = NewsListUIState(expectedFilteredNews)),
                awaitItem()
            )
            assertEquals(setOf("1"), viewModel.currentFilters)

            viewModel.setFilters(emptyList())
            val expectedUnfilteredNews = mappedNews.toImmutableList()
            assertEquals(
                UIState<NewsListUIState, String>(data = NewsListUIState(expectedUnfilteredNews)),
                awaitItem()
            )
            assertTrue(viewModel.currentFilters.isEmpty())

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `markAsRead - calls repository to read events`() = runTest {
        coEvery { repository.getEvents() } returns flowOf(emptyList())
        coEvery { repository.getReadEventIds() } returns flowOf(emptySet())
        // coEvery { repository.readEvents(any()) } coAnswers { } // Не обязательно, если relaxed = true

        val viewModel = createViewModel()

        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.markAsRead("id1", "id2")

        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { repository.readEvents("id1", "id2") }
    }

    @Test
    fun `state - correctly filters news when filters and news change`() = runTest {
        val viewModel = createViewModel()

        viewModel.state.test {
            skipItems(3) // initial, loading

            // 1. Загружаем новости, без фильтров
            eventsFlow.value = listOf(rawNews[0], rawNews[1])
            assertEquals(
                listOf(mappedNews[0], mappedNews[1]).toImmutableList(),
                awaitItem().data!!.newsList
            )

            // 2. Устанавливаем фильтр "cat1"
            viewModel.setFilters(listOf("1"))
            assertEquals(
                listOf(mappedNews[0]).toImmutableList(), // Только newsItem1 имеет cat1
                awaitItem().data!!.newsList
            )

            // 3. Приходят новые новости, фильтр "1" все еще активен
            eventsFlow.value = rawNews
            assertEquals(
                listOf(mappedNews[0], mappedNews[2]).toImmutableList(),
                awaitItem().data!!.newsList
            )

            // 4. Меняем фильтр на "2"
            viewModel.setFilters(listOf("2"))
            assertEquals(
                listOf(mappedNews[0], mappedNews[1]).toImmutableList(),
                awaitItem().data!!.newsList
            )

            cancelAndConsumeRemainingEvents()
        }
    }
}