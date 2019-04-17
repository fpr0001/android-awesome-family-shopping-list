package com.example.awesomefamilyshoppinglist.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.awesomefamilyshoppinglist.R
import com.example.awesomefamilyshoppinglist.main.MainActivity
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
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@SmallTest
class SplashActivityTest {

    @get:Rule
    var ruleForLiveData = InstantTaskExecutorRule()

    @get:Rule
    var ruleForDagger = espressoDaggerMockRule()

    @get:Rule
    val activityRule = IntentsTestRule<SplashActivity>(SplashActivity::class.java, false, false)

    @Mock
    lateinit var userRepository: UserRepository

    @Test
    fun test_initial_state() {
        onView(withId(R.id.button)).isGone()
        onView(withId(R.id.progressBar)).isVisible()
        onView(withId(R.id.imageView)).check(matches(isDisplayed()))
    }

    @Test
    fun test_user_already_logged_in() {
        `when`(userRepository.getCurrentUser()).then { Single.just(mock(FirebaseUser::class.java)) }
        activityRule.launchActivity(null)
        intended(hasComponent(SplashActivity::class.java.name))
//        onView(withId(R.id.button)).perform(click())
    }


//
//    @After
//    fun tearDown() {
//    }
}