package com.example.awesomefamilyshoppinglist.main

import android.content.ComponentName
import android.view.Gravity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions.open
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.contrib.NavigationViewActions.navigateTo
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.example.awesomefamilyshoppinglist.BuildConfig
import com.example.awesomefamilyshoppinglist.R
import com.example.awesomefamilyshoppinglist.di.AppComponentForTest
import com.example.awesomefamilyshoppinglist.di.modules.AppModuleForTest
import com.example.awesomefamilyshoppinglist.history.HistoryActivity
import com.example.awesomefamilyshoppinglist.model.Category
import com.example.awesomefamilyshoppinglist.model.Item
import com.example.awesomefamilyshoppinglist.model.User
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.splash.SplashActivity
import com.example.awesomefamilyshoppinglist.utils.app
import com.example.awesomefamilyshoppinglist.utils.espressoDaggerMockRule
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Single
import it.cosenonjaviste.daggermock.DaggerMock
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.Spy
import java.util.*
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    var ruleForDagger = DaggerMock.rule<AppComponentForTest>(AppModuleForTest()) {
        set { component ->
            component.inject(app)
            component.inject(this@MainActivityTest)
        }
        customizeBuilder<AppComponentForTest.Builder> { it.provideApplication(app) }
    }

    @get:Rule
    var ruleForLiveData = InstantTaskExecutorRule()

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java, false, false)

    @Spy
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var viewModel: MainContract.ViewModel

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
    fun should_FetchItems_When_onCreate() {
        activityRule.launchActivity(null)
        verify(viewModel).loadItems()
    }

    @Test
    fun should_DisplayItems_When_ItemsLiveDataPost() {
        val category = Category("", "fruits", Date())
        val categoryViewModel = CategoryViewModel(category)
        val user = User("", "felipe", Date(), "e@e.com", null, mutableListOf())
        val itemViewModel = ItemViewModel(Item("", "banana", Date(), user, null, null, null, category))

        `when`(viewModel.firebaseUserLiveData).thenReturn(MutableLiveData())
        `when`(viewModel.itemsLiveData).thenReturn(MutableLiveData(arrayListOf(categoryViewModel, itemViewModel)))

        activityRule.launchActivity(null)

        onView(withText("banana")).check(matches(isDisplayed()))
        onView(withText("fruits")).check(matches(isDisplayed()))

    }
}

inline fun <reified T> lambdaMock(): T = Mockito.mock(T::class.java)
