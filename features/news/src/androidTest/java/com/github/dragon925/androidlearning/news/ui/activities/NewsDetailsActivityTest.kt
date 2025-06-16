package com.github.dragon925.androidlearning.news.ui.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.core.os.bundleOf
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import androidx.work.Configuration
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import coil3.ColorImage
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.annotation.ExperimentalCoilApi
import coil3.test.FakeImageLoaderEngine
import com.github.dragon925.androidlearning.core.api.domain.models.Member
import com.github.dragon925.androidlearning.core.api.ui.UIState
import com.github.dragon925.androidlearning.news.R
import com.github.dragon925.androidlearning.news.di.components.NewsDetailsComponent
import com.github.dragon925.androidlearning.news.ui.fragments.MoneyDonationDialogFragment
import com.github.dragon925.androidlearning.news.ui.models.NewsDetailItem
import com.github.dragon925.androidlearning.news.ui.utils.DonationWorker
import com.github.dragon925.androidlearning.news.ui.viewmodels.NewsDetailsViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsDetailsActivityTest {

    @get:Rule
    val grantPermissionRule: GrantPermissionRule =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            GrantPermissionRule.grant()
        }

    private lateinit var mockViewModel: NewsDetailsViewModel
    private lateinit var mockComponentBuilder: NewsDetailsComponent.Builder

    private val uiState = MutableStateFlow<UIState<NewsDetailItem, String>>(UIState())

    private val testNewsId = "123"
    private val testNewsTitle = "Заголовок тестовой новости"
    private val testNewsDetails = NewsDetailItem(
        id = testNewsId,
        name = testNewsTitle,
        date = "01 января 2024",
        organizer = "Тестовая Организация",
        address = "Тестовый Адрес",
        phoneNumbers = listOf("123-456-789", "987-654-321"),
        photos = listOf(
            "example.com/photo1.jpg",
            "example.com/photo2.jpg",
            "example.com/photo3.jpg"
        ),
        description = "Детальное описание тестовой новости.",
        email = "test@example.com",
        website = "example.com/",
        members = listOf(
            Member(1, "Member 1", "example.com/avatar1.jpg"),
            Member(2, "Member 2", "example.com/avatar2.jpg")
        )
    )

    private lateinit var context: Context

    @OptIn(ExperimentalCoilApi::class, DelicateCoilApi::class)
    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        Intents.init()
        mockkObject(NewsDetailsActivity.Companion)

        mockViewModel = mockk(relaxed = true) {
            every { state } returns uiState
        }

        val mockViewModelFactory = mockk<NewsDetailsViewModel.Factory> {
            every { create(testNewsId) } returns mockViewModel
        }

        val mockComponent = mockk<NewsDetailsComponent>(relaxed = true) {
            every { inject(any()) } answers {
                val activity = firstArg<NewsDetailsActivity>()
                activity.viewModelFactory = mockViewModelFactory
            }
        }

        mockComponentBuilder = mockk {
            every { newsId(any()).deps(any()).build() } returns mockComponent
        }

        every { NewsDetailsActivity.Companion.getComponentBuilder() } returns mockComponentBuilder

        val config = Configuration.Builder()
            .setExecutor(SynchronousExecutor())
            .build()
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)

        val engine = FakeImageLoaderEngine.Builder()
            .intercept(
                predicate = { it is String && it.startsWith("example.com/photo") },
                image = ColorImage(Color.RED)
            )
            .intercept(
                predicate = { it is String && it.startsWith("example.com/avatar") },
                image = ColorImage(Color.BLUE)
            )
            .build()
        val loaderImageLoader = ImageLoader.Builder(context)
            .components { add(engine) }
            .build()
        SingletonImageLoader.setUnsafe(loaderImageLoader)
    }

    @After
    fun tearDown() {
        Intents.release()
        unmockkObject(NewsDetailsActivity.Companion)
    }

    private fun launchActivity(
        initialState: UIState<NewsDetailItem, String> = UIState()
    ): ActivityScenario<NewsDetailsActivity> {
        uiState.value = initialState

        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            NewsDetailsActivity::class.java
        ).apply {
            putExtra(NewsDetailsActivity.EXTRA_NEWS_ID, testNewsId)
            putExtra(NewsDetailsActivity.EXTRA_NEWS_TITLE, testNewsTitle)
        }

        return ActivityScenario.launch(intent)
    }

    @Test
    fun activityLaunches_displaysToolbarTitleFromIntent() {
        launchActivity().use {
            onView(withId(R.id.toolbar)).check(
                matches(hasDescendant(withText(testNewsTitle)))
            )
        }

        verify { NewsDetailsActivity.Companion.getComponentBuilder() }
    }

    @Test
    fun whenLoading_showsProgressBarAndHidesContent() {

        launchActivity(UIState(isLoading = true)).use {
            onView(withId(R.id.pi_loading)).check(matches(isDisplayed()))
            onView(withId(R.id.nsv_content)).check(matches(not(isDisplayed())))
        }

        verify { mockViewModel.state }
    }

    @Test
    fun whenDataLoaded_displaysNewsDetailsAndHidesProgressBar() {
        launchActivity(UIState(data = testNewsDetails)).use {
            onView(withId(R.id.pi_loading)).check(matches(not(isDisplayed())))
            onView(withId(R.id.nsv_content)).check(matches(isDisplayed()))

            onView(withId(R.id.tv_title)).check(matches(withText(testNewsDetails.name)))
            onView(withId(R.id.tv_date)).check(matches(withText(testNewsDetails.date)))
            onView(withId(R.id.tv_organization)).check(matches(withText(testNewsDetails.organizer)))
            onView(withId(R.id.tv_location)).check(matches(withText(testNewsDetails.address)))
            onView(withId(R.id.tv_phones)).check(
                matches(
                    withText(
                        testNewsDetails.phoneNumbers.joinToString(
                            "\n"
                        )
                    )
                )
            )
            onView(withId(R.id.tv_description)).check(matches(withText(testNewsDetails.description)))

            onView(withId(R.id.iv_image1)).check(matches(isDisplayed()))
            onView(withId(R.id.iv_image2)).check(matches(isDisplayed()))
            onView(withId(R.id.iv_image3)).check(matches(isDisplayed()))

            onView(withId(R.id.iv_avatar1)).check(matches(isVisible()))
            onView(withId(R.id.iv_avatar2)).check(matches(isVisible()))
            onView(withId(R.id.tv_more_avatars)).check(matches(not(isVisible())))
        }
    }

    @Test
    fun whenMoreThanFiveMembers_showsMoreAvatarsCount() {
        val manyMembers = testNewsDetails.copy(
            members = List(7) { Member(it, "Name $it", "example.com/avatar${it}.jpg") }
        )

        launchActivity(UIState(data = manyMembers)).use {
            onView(withId(R.id.iv_avatar5)).check(matches(isVisible()))
            onView(withId(R.id.tv_more_avatars)).check(
                matches(
                    allOf(
                        isVisible(),
                        withText(context.getString(R.string.more_count, 2))
                    )
                )
            )
        }
    }

    @Test
    fun helpButton_whenPermissionGranted_showsDonationDialog() {
        launchActivity(UIState(data = testNewsDetails)).use { scenario ->
            onView(withId(R.id.btn_help_money)).perform(click())

            scenario.onActivity { activity ->
                val fragment = activity.supportFragmentManager
                    .findFragmentByTag(MoneyDonationDialogFragment.TAG)
                assertNotNull(fragment)
            }
        }
    }

    @Test
    fun donationDialog_whenAmountEnteredAndConfirmed_enqueuesWorkManagerTask() {
        launchActivity(UIState(data = testNewsDetails)).use { scenario ->
            scenario.onActivity { activity ->
                val bundle = bundleOf(MoneyDonationDialogFragment.RESULT_VALUE to 150)
                activity.supportFragmentManager.setFragmentResult(
                    MoneyDonationDialogFragment.REQUEST_KEY,
                    bundle
                )
            }
        }

        val workManager = WorkManager.getInstance(context)
        runBlocking {
            val workInfos =
                workManager.getWorkInfosByTagFlow(DonationWorker::class.java.simpleName).first()

            assertFalse(workInfos.isEmpty())

            val work = workInfos.first()

            assertEquals(WorkInfo.State.ENQUEUED, work.state)
        }

    }

    private fun isVisible(): Matcher<in View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("is VISIBLE")
            }

            override fun matchesSafely(view: View): Boolean {
                return view.visibility == View.VISIBLE
            }
        }
    }
}