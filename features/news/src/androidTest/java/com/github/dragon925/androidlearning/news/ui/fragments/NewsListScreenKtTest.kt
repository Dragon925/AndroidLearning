package com.github.dragon925.androidlearning.news.ui.fragments

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.dragon925.androidlearning.core.api.ui.UIState
import com.github.dragon925.androidlearning.core.api.ui.theme.AppTheme
import com.github.dragon925.androidlearning.news.R
import com.github.dragon925.androidlearning.news.ui.activities.NewsDetailsActivity
import com.github.dragon925.androidlearning.news.ui.models.NewsItem
import com.github.dragon925.androidlearning.news.ui.models.NewsListUIState
import com.github.dragon925.androidlearning.news.ui.viewmodels.NewsViewModel
import com.github.dragon925.androidlearning.news.ui.viewmodels.UnreadNewsViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsListScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockNewsViewModel: NewsViewModel
    private lateinit var mockUnreadNewsViewModel: UnreadNewsViewModel
    private lateinit var navController: NavController

    private lateinit var newsString: String
    private lateinit var filterString: String

    private val uiState = MutableStateFlow(UIState<NewsListUIState, String>())

    private val testNewsItem1 = NewsItem(id = "1", title = "News Title 1", description = "Desc 1", date = "2023-01-01")
    private val testNewsItem2 = NewsItem(id = "2", title = "News Title 2", description = "Desc 2", date = "2023-01-02")
    private val testNewsList = persistentListOf(testNewsItem1, testNewsItem2)

    @Before
    fun setUp() {
        mockNewsViewModel = mockk(relaxed = true)
        mockUnreadNewsViewModel = mockk(relaxed = true)
        val context = ApplicationProvider.getApplicationContext<Context>()
        navController = TestNavHostController(context)
        every { mockNewsViewModel.state } returns uiState

        newsString = context.getString(R.string.news)
        filterString = context.getString(R.string.filter)
    }

    private fun launchScreen(
        initialState: UIState<NewsListUIState, String> = UIState()
    ) {
        uiState.value = initialState

        composeTestRule.setContent {
            navController.setGraph(R.navigation.news_nav)
            AppTheme {
                NewsListScreen(
                    newsViewModel = mockNewsViewModel,
                    unreadNewsViewModel = mockUnreadNewsViewModel,
                    navController = navController
                )
            }
        }
    }

    @Test
    fun shouldDisplayTopBarWithTitleAndFilterButton() {
        launchScreen()

        composeTestRule.onNodeWithText(newsString)
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription(filterString)
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun whenLoading_showsLoadingIndicator() {
        launchScreen(initialState = UIState(isLoading = true))

        composeTestRule.onNodeWithTag(TAG_LOADING_INDICATOR)
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun whenDataLoaded_showsNewsList() {
        val newsListUIState = NewsListUIState(newsList = testNewsList, readIds = persistentSetOf("1"))

        launchScreen(initialState = UIState(isLoading = false, data = newsListUIState))

        composeTestRule.onNodeWithText(testNewsItem1.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(testNewsItem2.title).assertIsDisplayed()

        verify { mockUnreadNewsViewModel.updateUnreadCount(1) }
    }

    @Test
    fun whenNoData_showsEmptyList() {
        launchScreen(initialState = UIState(data = NewsListUIState()))

        composeTestRule.onNodeWithTag(TAG_NEWS_ITEM).assertDoesNotExist()

        verify { mockUnreadNewsViewModel.updateUnreadCount(0) }
    }

    @Test
    fun shouldUpdateUnreadCount() {
        launchScreen(
            UIState(data = NewsListUIState(testNewsList))
        )

        verify { mockUnreadNewsViewModel.updateUnreadCount(2) }

        uiState.value = UIState(data = NewsListUIState(testNewsList, persistentSetOf("1")))

        composeTestRule.waitForIdle()

        verify { mockUnreadNewsViewModel.updateUnreadCount(1) }
    }

    @Test
    fun filterButtonClick_shouldNavigateToFilters() {
        launchScreen()

        composeTestRule.onNodeWithContentDescription(filterString).performClick()

        assertEquals(R.id.filterFragment, navController.currentDestination?.id)
    }

    @Test
    fun newsItemClick_shouldNavigateToDetails() {
        launchScreen(UIState(data = NewsListUIState((testNewsList))))

        composeTestRule.onNodeWithText(testNewsItem1.title).performClick()

        verify { mockNewsViewModel.markAsRead(testNewsItem1.id) }

        assertEquals(R.id.newsDetailsActivity, navController.currentDestination?.id)

        val args = navController.currentBackStackEntry?.arguments
        assertEquals(testNewsItem1.id, args?.getString(NewsDetailsActivity.EXTRA_NEWS_ID))
        assertEquals(testNewsItem1.title, args?.getString(NewsDetailsActivity.EXTRA_NEWS_TITLE))
    }
}