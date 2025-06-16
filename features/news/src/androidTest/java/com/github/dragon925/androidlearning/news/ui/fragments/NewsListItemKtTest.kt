package com.github.dragon925.androidlearning.news.ui.fragments

import android.content.Context
import android.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil3.ColorImage
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.annotation.ExperimentalCoilApi
import coil3.test.FakeImageLoaderEngine
import com.github.dragon925.androidlearning.core.api.ui.theme.AppTheme
import com.github.dragon925.androidlearning.news.R
import com.github.dragon925.androidlearning.news.ui.models.NewsItem
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsListItemKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockOnClick: () -> Unit

    private lateinit var eventPhotoDescription: String
    private lateinit var eventDividerDescription: String
    private lateinit var eventDateDescription: String

    private val testNewsItem = NewsItem(
        id = "1",
        image = "http://example.com/image.jpg",
        title = "Заголовок тестовой новости для проверки",
        description = "Это очень детальное описание тестовой новости, которое должно быть достаточно длинным, чтобы проверить переполнение и многоточие.",
        date = "Январь 01, 2024"
    )

    @OptIn(ExperimentalCoilApi::class, DelicateCoilApi::class)
    @Before
    fun setUp() {
        mockOnClick = mockk(relaxed = true)
        with(ApplicationProvider.getApplicationContext<Context>()) {
            eventPhotoDescription = getString(R.string.description_event_photo)
            eventDividerDescription = getString(R.string.description_event_divider)
            eventDateDescription = getString(R.string.description_event_date)

            val engine = FakeImageLoaderEngine.Builder()
                .intercept(testNewsItem.image!!, ColorImage(Color.RED))
                .build()
            val loaderImageLoader = ImageLoader.Builder(this)
                .components { add(engine) }
                .build()
            SingletonImageLoader.setUnsafe(loaderImageLoader)
        }
    }

    private fun launchComposable(newsItem: NewsItem, modifier: Modifier = Modifier) {
        composeTestRule.setContent {
            AppTheme {
                NewsListItem(
                    newsItem = newsItem,
                    modifier = modifier,
                    onClick = mockOnClick
                )
            }
        }
    }

    @Test
    fun newsListItem_displaysCorrectData() {
        launchComposable(testNewsItem)

        composeTestRule.onNodeWithContentDescription(eventPhotoDescription)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(testNewsItem.title)
            .assertIsDisplayed()

         composeTestRule.onNodeWithContentDescription(eventDividerDescription)
             .assertIsDisplayed()

        composeTestRule.onNodeWithText(testNewsItem.description, substring = true)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(testNewsItem.date)
            .assertIsDisplayed()

         composeTestRule.onNodeWithContentDescription(eventDateDescription)
             .assertIsDisplayed()
    }

    @Test
    fun newsListItem_isClickableAndInvokesOnClick() {
        launchComposable(testNewsItem)

        composeTestRule.onNodeWithText(testNewsItem.title)
            .assertHasClickAction()

        composeTestRule.onNodeWithText(testNewsItem.title).performClick()

        verify(exactly = 1) { mockOnClick() }
    }
}