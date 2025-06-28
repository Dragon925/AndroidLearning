package com.github.dragon925.androidlearning.news.ui.utils

import android.content.Context
import android.content.res.Resources
import com.github.dragon925.androidlearning.core.api.domain.models.Event
import com.github.dragon925.androidlearning.news.ui.models.NewsDetailItem
import com.github.dragon925.androidlearning.news.ui.models.NewsItem
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MappersKtTest {

    private lateinit var context: Context
    private lateinit var resources: Resources

    private val testEventSingleDay = Event(
        id = "event1",
        name = "Summer Festival",
        description = "A great festival in summer.",
        startDate = LocalDate(2025, 7, 15),
        endDate = LocalDate(2025, 7, 15),
        categoryIds = listOf("music", "festival"),
        organizer = "Tech Inc.",
        address = "123 Main St",
        phoneNumbers = listOf("555-0100"),
        email = "info@techinc.com",
        website = "http://techinc.com",
        photos = listOf("photo1.jpg", "photo2.jpg")
    )

    private val testNewsItemSingleDay = NewsItem(
        id = "event1",
        title = "Summer Festival",
        description = "A great festival in summer.",
        date = "Осталось 14 дней (15.07)",
        categoryIds = persistentSetOf("music", "festival"),
        image = "photo1.jpg"
    )

    private val testNewsDetailsItemSingleDay = NewsDetailItem(
        id = "event1",
        name = "Summer Festival",
        description = "A great festival in summer.",
        date = "Осталось 14 дней (15.07)",
        organizer = "Tech Inc.",
        address = "123 Main St",
        phoneNumbers = listOf("555-0100"),
        email = "info@techinc.com",
        website = "http://techinc.com",
        photos = listOf("photo1.jpg", "photo2.jpg")
    )

    private val testEventMultiDay = Event(
        id = "event2",
        name = "Winter Conference",
        description = "Annual winter conference.",
        startDate = LocalDate(2025, 12, 1),
        endDate = LocalDate(2025, 12, 3),
        categoryIds = listOf("conference", "tech"),
        organizer = "Tech Inc.",
        address = "123 Main St",
        phoneNumbers = listOf("555-0100"),
        email = "info@techinc.com",
        website = "http://techinc.com",
        photos = listOf("photo3.jpg", "photo4.jpg")
    )

    private val testNewsItemMultiDay = NewsItem(
        id = "event2",
        title = "Winter Conference",
        description = "Annual winter conference.",
        date = "Осталось 16 дней (01.12 – 03.12)",
        categoryIds = persistentSetOf("conference", "tech"),
        image = "photo3.jpg"
    )
    private val testNewsDetailsItemMultiDay = NewsDetailItem(
        id = "event2",
        name = "Winter Conference",
        description = "Annual winter conference.",
        date = "Осталось 16 дней (01.12 – 03.12)",
        organizer = "Tech Inc.",
        address = "123 Main St",
        phoneNumbers = listOf("555-0100"),
        email = "info@techinc.com",
        website = "http://techinc.com",
        photos = listOf("photo3.jpg", "photo4.jpg")
    )

    private val testMonthsFull = arrayOf(
        "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
        "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
    )

    @Before
    fun setUp() {
        context = mockk()
        resources = mockk()
        every { context.resources } returns resources

        every { resources.getString(any()) } returns "Осталось"
        every { resources.getQuantityString(any(), any(), any()) } answers {
            when (val quantity = arg<Int>(1)) {
                1, 21, 31 -> "$quantity день"
                in 2..4, in 22..24, in 32..34 -> "$quantity дня"
                else -> "$quantity дней"
            }
        }
        every { resources.getStringArray(any()) } returns testMonthsFull

        mockkObject(Clock.System)
    }

    @After
    fun tearDown() {
        unmockkObject(Clock.System)
    }

    private fun setCurrentDate(year: Int, month: Int, day: Int) {
        val instant = LocalDate(year, month, day)
            .atStartOfDayIn(TimeZone.currentSystemDefault())
        every { Clock.System.now() } returns instant
    }

    @Test
    fun `toNewsItem - maps Event to NewsItem correctly with single-day event`() {
        setCurrentDate(2025, 7, 1)

        val newsItem = testEventSingleDay.toNewsItem(context)

        assertEquals(testNewsItemSingleDay, newsItem)
    }

    @Test
    fun `toNewsItem - maps Event to NewsItem correctly with multi-day event`() {
        setCurrentDate(2025, 11, 15)

        val newsItem = testEventMultiDay.toNewsItem(context)

        assertEquals(testNewsItemMultiDay, newsItem)
    }

    @Test
    fun `toNewsDetailItem - maps Event to NewsDetailItem correctly with single-day event`() {
        setCurrentDate(2025, 7, 1)

        val detailItem = testEventSingleDay.toNewsDetailItem(context)

        assertEquals(testNewsDetailsItemSingleDay, detailItem)

    }

    @Test
    fun `toNewsDetailItem - maps Event to NewsDetailItem correctly with multi-day event`() {
        setCurrentDate(2025, 11, 15)

        val detailItem = testEventMultiDay.toNewsDetailItem(context)

        assertEquals(testNewsDetailsItemMultiDay, detailItem)
    }

    @Test
    fun `toNewsItem - less than 32 days, single day event`() {
        setCurrentDate(2025, 7, 1)

        val date = testEventSingleDay.toNewsItem(context).date

        assertEquals("Осталось 14 дней (15.07)", date)
    }

    @Test
    fun `toNewsItem - less than 32 days, multi day event`() {
        setCurrentDate(2025, 11, 15)

        val date = testEventMultiDay.toNewsItem(context).date

        assertEquals("Осталось 16 дней (01.12 – 03.12)", date)
    }

    @Test
    fun `getDateString - exactly 1 day left`() {
        setCurrentDate(2025, 7, 14)

        val date = testEventSingleDay.toNewsItem(context).date

        assertEquals("Осталось 1 день (15.07)", date)
    }

    @Test
    fun `getDateString - 2 days left`() {
        setCurrentDate(2025, 7, 13)

        val date = testEventSingleDay.toNewsItem(context).date

        assertEquals("Осталось 2 дня (15.07)", date)
    }

    @Test
    fun `getDateString - 5 days left`() {
        setCurrentDate(2025, 7, 10)

        val date = testEventSingleDay.toNewsItem(context).date

        assertEquals("Осталось 5 дней (15.07)", date)
    }

    @Test
    fun `getDateString - more than 31 days, single day event`() {
        setCurrentDate(2024, 1, 1)

        val date = testEventSingleDay.toNewsItem(context).date

        assertEquals("Июль 15, 2025", date)
    }

    @Test
    fun `getDateString - more than 31 days, multi day event`() {
        setCurrentDate(2024, 1, 1)

        val date = testEventMultiDay.toNewsItem(context).date

        assertEquals("Декабрь 01, 2025 – Декабрь 03, 2025", date)
    }
}