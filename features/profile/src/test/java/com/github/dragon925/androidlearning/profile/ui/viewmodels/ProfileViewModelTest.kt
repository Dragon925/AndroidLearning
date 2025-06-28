package com.github.dragon925.androidlearning.profile.ui.viewmodels

import app.cash.turbine.test
import com.github.dragon925.androidlearning.core.api.domain.models.Mapper
import com.github.dragon925.androidlearning.core.api.ui.UIState
import com.github.dragon925.androidlearning.profile.domain.models.Profile
import com.github.dragon925.androidlearning.profile.domain.repositories.ProfileRepository
import com.github.dragon925.androidlearning.profile.ui.models.ProfileUIState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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
import kotlinx.datetime.LocalDate
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    private lateinit var repository: ProfileRepository
    private lateinit var mapper: Mapper<Profile, ProfileUIState>

    private val testProfileId = "test123"
    private val rawProfile = Profile(
        testProfileId,
        "Test Name",
        LocalDate(2000, 1, 1)
    )
    private val mappedProfile = ProfileUIState(
        testProfileId,
        "Test Name",
        "Jan 1, 2000"
    )

    private fun createViewModel() = ProfileViewModel(testProfileId, repository, mapper)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        repository = mockk()
        mapper = mockk()

        every { mapper.invoke(rawProfile) } returns mappedProfile
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init - state emits initial (not loading, empty data), then loading, then success with profile data`() = runTest {
        coEvery { repository.loadProfile(testProfileId) } returns flowOf(rawProfile)

        createViewModel().state.test {
            assertEquals(UIState<ProfileUIState, String>(), awaitItem())

            assertEquals(UIState<ProfileUIState, String>(isLoading = true), awaitItem())

            assertEquals(UIState<ProfileUIState, String>(), awaitItem())

            assertEquals(UIState<ProfileUIState, String>(data = mappedProfile), awaitItem())

            cancelAndConsumeRemainingEvents()
        }

        coVerify(exactly = 1) { repository.loadProfile(testProfileId) }
        coVerify(exactly = 1) { mapper.invoke(rawProfile) }
    }

    @Test
    fun `init - state emits initial, loading, then error state (loading true, null data) when repository throws exception`() = runTest {
            val exception = RuntimeException("Network Error")
            coEvery { repository.loadProfile(testProfileId) } returns flow { throw exception }

            createViewModel().state.test {
                assertEquals(UIState<ProfileUIState, String>(), awaitItem()
                )

                assertEquals(UIState<ProfileUIState, String>(isLoading = true), awaitItem()
                )

                cancelAndConsumeRemainingEvents()
            }
            coVerify(exactly = 1) { repository.loadProfile(testProfileId) }
            coVerify(exactly = 0) { mapper.invoke(any()) }
        }

    @Test
    fun `init - when profile is EMPTY, data in UIState is null`() = runTest {
            coEvery { repository.loadProfile(testProfileId) } returns flowOf(rawProfile)

            createViewModel().state.test {
                val initialState = awaitItem()
                assertEquals(false, initialState.isLoading)
                assertNull("Data should be null when profile is EMPTY", initialState.data)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `init - when profile is NOT EMPTY, data in UIState is the profile data`() = runTest {
            coEvery { repository.loadProfile(testProfileId) } returns flowOf(rawProfile)

            createViewModel().state.test {
                repeat(3) {
                    awaitItem()
                }

                val successState = awaitItem()

                assertEquals(false, successState.isLoading)
                assertEquals(mappedProfile, successState.data)
                cancelAndConsumeRemainingEvents()
            }
        }
}