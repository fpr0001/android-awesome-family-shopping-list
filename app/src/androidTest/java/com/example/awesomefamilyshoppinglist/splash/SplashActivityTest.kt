package com.example.awesomefamilyshoppinglist.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.rule.ActivityTestRule
import com.example.awesomefamilyshoppinglist.R
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.utils.espressoDaggerMockRule
import com.example.awesomefamilyshoppinglist.utils.isGone
import com.example.awesomefamilyshoppinglist.utils.isVisible
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.Spy

@SmallTest
@RunWith(AndroidJUnit4::class)
class SplashActivityTest {

    @get:Rule
    var ruleForDagger = espressoDaggerMockRule()

    @get:Rule
    var ruleForLiveData = InstantTaskExecutorRule()

    @get:Rule
    val activityRule = ActivityTestRule<SplashActivity>(SplashActivity::class.java, false, false)

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var splashRouter: SplashContract.Router

    @Test
    fun test_VisibilityOfViews_When_InitialState() {
        `when`(userRepository.getCurrentUser()).thenReturn(Single.error(RuntimeException()))
        doNothing().`when`(splashRouter).goToLogin()
        activityRule.launchActivity(null)
        onView(withId(R.id.button)).isGone()
        onView(withId(R.id.progressBar)).isGone()
        onView(withId(R.id.imageView)).check(matches(isDisplayed()))
    }

    @Test
    fun should_LaunchMain_When_UserIsLoggedIn() {
        `when`(userRepository.getCurrentUser()).thenReturn(Single.just(mock(FirebaseUser::class.java)))
        activityRule.launchActivity(null)
        verify(splashRouter).goToMain()
    }

    @Test
    fun should_LaunchLogin_When_UserIsLoggedOut() {
        `when`(userRepository.getCurrentUser()).thenReturn(Single.error(RuntimeException()))
        activityRule.launchActivity(null)
        verify(splashRouter).goToLogin()
    }


}