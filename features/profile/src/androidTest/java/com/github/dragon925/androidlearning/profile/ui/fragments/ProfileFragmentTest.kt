package com.github.dragon925.androidlearning.profile.ui.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasType
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import coil3.ColorImage
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.annotation.ExperimentalCoilApi
import coil3.test.FakeImageLoaderEngine
import com.github.dragon925.androidlearning.core.api.ui.UIState
import com.github.dragon925.androidlearning.core.api.ui.registerActionLauncher
import com.github.dragon925.androidlearning.profile.R
import com.github.dragon925.androidlearning.profile.di.ProfileComponent
import com.github.dragon925.androidlearning.profile.ui.models.FriendItem
import com.github.dragon925.androidlearning.profile.ui.models.ProfileUIState
import com.github.dragon925.androidlearning.profile.ui.viewmodels.ProfileViewModel
import com.github.dragon925.androidlearning.profile.utils.TestApp
import com.google.android.material.appbar.MaterialToolbar
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkObject
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.github.dragon925.androidlearning.core.api.R as CoreR

@RunWith(AndroidJUnit4::class)
class ProfileFragmentTest {

    private val testUserId = "testUserId"

    private val uiState = MutableStateFlow<UIState<ProfileUIState, String>>(UIState())

    private val viewModel: ProfileViewModel = mockk(relaxed = true) {
        every { state } returns uiState
    }
    private val viewModelFactory: ProfileViewModel.Factory = mockk {
        every { create(testUserId) } returns viewModel
    }

    private val component: ProfileComponent = mockk {
        justRun { inject(any()) }
    }
    private val componentBuilder: ProfileComponent.Builder = mockk {
        every { deps(any()).build() } returns component
    }

    private val testBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)

    private val profileFragmentFactory = object : FragmentFactory() {
        override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
            val fragment = super.instantiate(classLoader, className)

            if (fragment is ProfileFragment) {
                fragment.componentBuilder = componentBuilder
                fragment.viewModelFactory = viewModelFactory
            }

            return fragment
        }
    }

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.CAMERA
    )

    @Before
    fun setUp() {
        uiState.value = UIState()
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    private fun launchFragment() = launchFragmentInContainer<ProfileFragment>(
        fragmentArgs = bundleOf("userId" to testUserId),
        factory = profileFragmentFactory,
        themeResId = CoreR.style.Theme_AndroidLearning,
        initialState = Lifecycle.State.CREATED
    )

    @OptIn(ExperimentalCoilApi::class, DelicateCoilApi::class)
    @Test
    fun displayProfileData_WhenStateIsSuccess() {
        val scenario = launchFragment()
        val data = ProfileUIState(
            id = testUserId,
            name = "John Doe",
            birthday = "01.01.1990",
            fieldOfActivity = "Android Developer",
            avatar = "http://example.com/avatar.jpg",
            friends = listOf(
                FriendItem("1", "http://example.com/friend_avatar.jpg", "Friend One")
            )
        )
        val engine = FakeImageLoaderEngine.Builder()
            .intercept(data.avatar, ColorImage(Color.RED))
            .intercept(data.friends[0].avatar, ColorImage(Color.BLUE))
            .build()
        val loaderImageLoader = ImageLoader.Builder(ApplicationProvider.getApplicationContext())
            .components { add(engine) }
            .build()
        SingletonImageLoader.setUnsafe(loaderImageLoader)

        scenario.moveToState(Lifecycle.State.RESUMED)
        uiState.value = UIState(data = data)

        onView(withId(R.id.iv_avatar)).check(matches(hasDrawable()))
        onView(withId(R.id.tv_name)).check(matches(withText(data.name)))
        onView(withId(R.id.tv_birthday)).check(matches(withText(data.birthday)))
        onView(withId(R.id.tv_field_of_activity)).check(matches(withText(data.fieldOfActivity)))
        onView(withId(R.id.rv_friends)).check(matches(hasChildCount(data.friends.size)))
        onView(withText(data.friends[0].name)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_friend_avatar)).check(matches(hasDrawable()))
    }


    @Test
    fun editButton_shouldOpenAvatarDialog() {
        val scenario = launchFragment()
        scenario.moveToState(Lifecycle.State.RESUMED)

        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        scenario.onFragment { fragment ->
            navController.setGraph(R.navigation.profile_nav)
            navController.setCurrentDestination(R.id.profileFragment)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.toolbar)).perform(clickMenuItem(R.id.action_edit))

        assertEquals(R.id.editAvatarDialogFragment, navController.currentDestination?.id)
    }

    @Test
    fun takePhoto_launchesPermissionRequest() {
        mockkStatic("com.github.dragon925.androidlearning.core.api.ui.UtilsKt")

        val scenario = launchFragment()

        val launcher: ActivityResultLauncher<String> = mockk(relaxed = true)
        scenario.onFragment { fragment ->
            every {
                fragment.registerActionLauncher<ProfileFragment, String, Boolean>(any(), any())
            } returns launcher
        }

        scenario.moveToState(Lifecycle.State.RESUMED)

        scenario.onFragment { fragment ->

            val bundle = bundleOf(
                EditAvatarDialogFragment.RESULT_TYPE to EditAvatarDialogFragment.RESULT_CODE_TAKE_PHOTO
            )
            fragment.parentFragmentManager.setFragmentResult(
                EditAvatarDialogFragment.REQUEST_KEY,
                bundle
            )
        }

        verify { launcher.launch(android.Manifest.permission.CAMERA) }

        unmockkStatic("com.github.dragon925.androidlearning.core.api.ui.UtilsKt")
    }

    @Test
    fun takePhoto_whenPermissionGranted_shouldLaunchCamera() {
        mockkStatic("com.github.dragon925.androidlearning.core.api.ui.UtilsKt")

        val scenario = launchFragment()

        val launcher: ActivityResultLauncher<Void?> = mockk(relaxed = true)

        var callback: ActivityResultCallback<Bitmap?>? = null

        scenario.onFragment { fragment ->
            every {
                fragment.registerActionLauncher(
                    any<ActivityResultContracts.TakePicturePreview>(),
                    any<ActivityResultCallback<Bitmap?>>()
                )
            } answers {
                if (it.invocation.args[1] is ActivityResultContracts.TakePicturePreview) {
                    callback = thirdArg()
                    launcher
                } else {
                    callOriginal()
                }
            }
        }

        scenario.moveToState(Lifecycle.State.RESUMED)

        scenario.onFragment { fragment ->

            val bundle = bundleOf(
                EditAvatarDialogFragment.RESULT_TYPE to EditAvatarDialogFragment.RESULT_CODE_TAKE_PHOTO
            )
            fragment.parentFragmentManager.setFragmentResult(
                EditAvatarDialogFragment.REQUEST_KEY,
                bundle
            )

            callback?.onActivityResult(testBitmap)
        }


        onView(withId(R.id.iv_avatar)).check(matches(withBitmap(testBitmap)))

        verify { launcher.launch(null) }

        unmockkStatic("com.github.dragon925.androidlearning.core.api.ui.UtilsKt")
    }


    @Test
    fun deletePhoto_shouldDeleteAvatar() {

        val scenario = launchFragment()

        scenario.moveToState(Lifecycle.State.RESUMED)

        scenario.onFragment { fragment ->
            val bundle = bundleOf(
                EditAvatarDialogFragment.RESULT_TYPE to EditAvatarDialogFragment.RESULT_CODE_DELETE_PHOTO
            )
            fragment.parentFragmentManager.setFragmentResult(
                EditAvatarDialogFragment.REQUEST_KEY,
                bundle
            )
        }

        onView(withId(R.id.iv_avatar)).check(matches(withDrawable(R.drawable.image_user)))
    }

    @Test
    fun choosePhoto_whenPhotoPickerNotAvailable_launchesGalleryIntent() {
        mockkObject(PickVisualMedia.Companion)
        every { PickVisualMedia.Companion.isPhotoPickerAvailable(any()) } returns false
        val scenario = launchFragment()

        scenario.moveToState(Lifecycle.State.RESUMED)

        scenario.onFragment { fragment ->
            val bundle = bundleOf(
                EditAvatarDialogFragment.RESULT_TYPE to EditAvatarDialogFragment.RESULT_CODE_CHOOSE_PHOTO
            )
            fragment.parentFragmentManager.setFragmentResult(
                EditAvatarDialogFragment.REQUEST_KEY,
                bundle
            )
        }

        intended(
            allOf(
                hasAction(Intent.ACTION_GET_CONTENT),
                hasType("image/*")
            )
        )
        unmockkObject(PickVisualMedia.Companion)
    }

    @Test
    fun choosePhoto_whenPhotoPickerAvailable_launchesPhotoPicker() {
        mockkObject(PickVisualMedia.Companion)
        mockkStatic("com.github.dragon925.androidlearning.core.api.ui.UtilsKt")

        every { PickVisualMedia.Companion.isPhotoPickerAvailable(any()) } returns true

        val app = ApplicationProvider.getApplicationContext<TestApp>()

        val testUri = Uri.parse("android.resource://${app.packageName}/${R.drawable.image_user}")
        val launcher: ActivityResultLauncher<PickVisualMediaRequest> = mockk(relaxed = true)
        var callback: ActivityResultCallback<Uri?>? = null
        val scenario = launchFragment()

        scenario.onFragment { fragment ->
            every {
                fragment.registerActionLauncher(
                    any<PickVisualMedia>(),
                    any()
                )
            } answers {
                if (it.invocation.args[1] is PickVisualMedia) {
                    callback = thirdArg()
                    launcher
                } else {
                    callOriginal()
                }
            }
        }

        scenario.moveToState(Lifecycle.State.RESUMED)

        scenario.onFragment { fragment ->
            val bundle = bundleOf(
                EditAvatarDialogFragment.RESULT_TYPE to EditAvatarDialogFragment.RESULT_CODE_CHOOSE_PHOTO
            )
            fragment.parentFragmentManager.setFragmentResult(
                EditAvatarDialogFragment.REQUEST_KEY,
                bundle
            )

            callback?.onActivityResult(testUri)
        }

        onView(withId(R.id.iv_avatar)).check(matches(hasDrawable()))

        verify {
            launcher.launch(
                match { request ->
                    request.mediaType == PickVisualMedia.ImageOnly
                }
            )
        }

        unmockkStatic("com.github.dragon925.androidlearning.core.api.ui.UtilsKt")
        unmockkObject(PickVisualMedia.Companion)
    }

    private fun clickMenuItem(@IdRes menuItemId: Int) = object : ViewAction {
        override fun getConstraints() = isAssignableFrom(MaterialToolbar::class.java)
        override fun getDescription() = "Click toolbar menu item"
        override fun perform(uiController: UiController, view: View) {
            (view as MaterialToolbar).findViewById<View>(menuItemId).performClick()
        }
    }

    private fun hasDrawable(): Matcher<in View> {
        return object : BoundedMatcher<View, ImageView>(ImageView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has drawable")
            }

            override fun matchesSafely(view: ImageView): Boolean {
                return view.drawable != null
            }
        }
    }

    private fun withDrawable(@DrawableRes id: Int): Matcher<in View> {
        return object : BoundedMatcher<View, ImageView>(ImageView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has drawable with id $id")
            }

            override fun matchesSafely(view: ImageView): Boolean {
                val expected = ContextCompat.getDrawable(view.context, id)?.toBitmapOrNull(100, 100)
                return view.drawable?.toBitmapOrNull(100, 100)?.sameAs(expected) ?: false

            }
        }
    }

    private fun withBitmap(bitmap: Bitmap): Matcher<in View> {
        return object : BoundedMatcher<View, ImageView>(ImageView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has drawable with bitmap")
            }

            override fun matchesSafely(view: ImageView): Boolean {
                return view.drawable.toBitmap(bitmap.width, bitmap.height, bitmap.config)
                    .sameAs(bitmap)
            }
        }
    }
}