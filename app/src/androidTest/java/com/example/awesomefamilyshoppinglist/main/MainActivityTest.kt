package com.example.awesomefamilyshoppinglist.main

import android.view.Gravity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions.open
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.contrib.NavigationViewActions.navigateTo
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.example.awesomefamilyshoppinglist.R
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.utils.espressoDaggerMockRule
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    var ruleForDagger = espressoDaggerMockRule()

    @get:Rule
    var ruleForLiveData = InstantTaskExecutorRule()

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java, false, false)

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var mainRouter: MainContract.Router

    @Before
    fun before() {
        `when`(userRepository.getCurrentUser()).thenReturn(Single.just(mock(FirebaseUser::class.java)))
    }

    @Test
    fun should_LaunchSplashScreen_When_LogoutIsTapped() {

        `when`(userRepository.logout()).thenReturn(Completable.complete())

        activityRule.launchActivity(null)

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
            .perform(open()) // Open Drawer

        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.action_logout))
        verify(mainRouter).goToSplash(activityRule.activity)
    }

    @Test
    fun clickOnAndroidHomeIcon_OpensNavigation() {
        activityRule.launchActivity(null)

        // Check that left drawer is closed at startup
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.

        // Open Drawer
        val navigateUpDesc = activityRule.activity
            .getString(R.string.navigation_drawer_open)
        onView(withContentDescription(navigateUpDesc)).perform(click())

        // Check if drawer is open
        onView(withId(R.id.drawer_layout))
            .check(matches(isOpen(Gravity.LEFT))) // Left drawer is open open.
    }

}