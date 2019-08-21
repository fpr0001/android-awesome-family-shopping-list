package com.example.awesomefamilyshoppinglist.main

import android.view.Gravity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions.open
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.contrib.NavigationViewActions.navigateTo
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.example.awesomefamilyshoppinglist.BuildConfig
import com.example.awesomefamilyshoppinglist.R
import com.example.awesomefamilyshoppinglist.repositories.FakeCategoryRepository
import com.example.awesomefamilyshoppinglist.repositories.FakeItemsRepository
import com.example.awesomefamilyshoppinglist.repositories.FakeUserRepository
import com.example.awesomefamilyshoppinglist.utils.app
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import javax.inject.Inject

@LargeTest
class MainActivityTest {

    @get:Rule
    var ruleForLiveData = InstantTaskExecutorRule()

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java, false, false)

    @Inject
    lateinit var viewModel: MainContract.ViewModel

    @Inject
    lateinit var fakeUserRepository: FakeUserRepository

    @Inject
    lateinit var fakeCategoryRepository: FakeCategoryRepository

    @Inject
    lateinit var fakeItemsRepository: FakeItemsRepository

    @Inject
    lateinit var mainRouterSpy: MainContract.Router

    @Before
    fun before() {
        app.component.inject(this)
    }

    @After
    fun after() {
        clearInvocations(viewModel)
        if (fakeUserRepository.internalFirebaseUserLiveData.value == null) {
            fakeUserRepository.internalFirebaseUserLiveData.postValue(fakeUserRepository.fakeCurrentUser)
        }
    }

    @Test
    fun should_LaunchSplashScreen_When_LogoutIsTapped() {
        activityRule.launchActivity(null)

        onView(withId(R.id.drawer_layout)).perform(open())
        doNothing().`when`(mainRouterSpy).goToSplash(activityRule.activity)

        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.action_logout))

        verify(mainRouterSpy).goToSplash(activityRule.activity)
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
        activityRule.launchActivity(null)

        onView(withId(R.id.drawer_layout)).perform(open())

        doNothing().`when`(mainRouterSpy).goToHistory(activityRule.activity)

        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.action_history))

        verify(mainRouterSpy).goToHistory(activityRule.activity)
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
    fun should_FetchItems_When_onCreate() {
        activityRule.launchActivity(null)
        verify(viewModel).loadItems()
    }

    @Test
    fun should_DisplayItems_When_ItemsLiveDataPost() {

        activityRule.launchActivity(null)

        onView(withText(fakeItemsRepository.item.name)).check(matches(isDisplayed()))
        onView(withText(fakeCategoryRepository.category.name)).check(matches(isDisplayed()))

    }
}