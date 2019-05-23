package com.example.awesomefamilyshoppinglist.main

import android.content.ComponentName
import android.view.Gravity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions.open
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.contrib.NavigationViewActions.navigateTo
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.example.awesomefamilyshoppinglist.BuildConfig
import com.example.awesomefamilyshoppinglist.R
import com.example.awesomefamilyshoppinglist.history.HistoryActivity
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.splash.SplashActivity
import com.example.awesomefamilyshoppinglist.utils.espressoDaggerMockRule
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

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

    @Before
    fun before() {
        `when`(userRepository.getCurrentFirebaseUser()).thenReturn(Single.just(mock(FirebaseUser::class.java)))
    }

    @Test
    fun should_LaunchSplashScreen_When_LogoutIsTapped() {
        Intents.init()
        `when`(userRepository.logout()).thenReturn(Completable.complete())

        activityRule.launchActivity(null)

        onView(withId(R.id.drawer_layout)).perform(open())

        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.action_logout))

        val componentName = ComponentName(activityRule.activity, SplashActivity::class.java)
        Intents.intended(IntentMatchers.hasComponent(componentName))
        Intents.release()
    }

    @Test
    fun should_OpenDrawer_When_AndroidHomeTapped() {
        activityRule.launchActivity(null)

        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT)))

        val navigateUpDesc = activityRule.activity
            .getString(R.string.navigation_drawer_open)
        onView(withContentDescription(navigateUpDesc)).perform(click())

        onView(withId(R.id.drawer_layout))
            .check(matches(isOpen(Gravity.LEFT)))
    }

    @Test
    fun should_CloseDrawer_When_AndroidBackTapped() {
        activityRule.launchActivity(null)

        val navigateUpDesc = activityRule.activity
            .getString(R.string.navigation_drawer_open)
        onView(withContentDescription(navigateUpDesc)).perform(click())

        Espresso.pressBack()

        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT)))
    }

    @Test
    fun should_LaunchHistoryScreen_When_HistoryIsTapped() {
        Intents.init()

        activityRule.launchActivity(null)

        onView(withId(R.id.drawer_layout)).perform(open())

        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.action_history))

        val componentName = ComponentName(activityRule.activity, HistoryActivity::class.java)
        Intents.intended(IntentMatchers.hasComponent(componentName))
        Intents.release()
    }


    @Test
    fun should_ShowAppVersion_When_DrawerIsOpened() {
        activityRule.launchActivity(null)

        val navigateUpDesc = activityRule.activity
            .getString(R.string.navigation_drawer_open)
        onView(withContentDescription(navigateUpDesc)).perform(click())

        onView(withId(R.id.text_view_version))
            .check(matches(isDisplayed()))
            .check(matches(withText("v" + BuildConfig.VERSION_NAME)))
    }

    @Test
    fun should_DisplayShoppingItems_When_onCreate() {
        activityRule.launchActivity(null)

        //fetch items from repository here

        onView(withId(R.id.recycler_view))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(0))

        onView(withText("Banana")).check(matches(isDisplayed()))
        onView(withText("Fruits")).check(matches(isDisplayed()))

    }
}