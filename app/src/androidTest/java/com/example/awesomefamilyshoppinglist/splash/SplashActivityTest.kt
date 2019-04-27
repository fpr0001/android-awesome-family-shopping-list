package com.example.awesomefamilyshoppinglist.splash

import android.app.Activity
import android.app.Instrumentation
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.ActivityResultFunction
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.VerificationMode
import androidx.test.espresso.intent.VerificationModes
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.rule.ActivityTestRule
import com.example.awesomefamilyshoppinglist.R
import com.example.awesomefamilyshoppinglist.di.AppComponentForTest
import com.example.awesomefamilyshoppinglist.di.modules.AppModuleForTest
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.repositories.UserRepositoryImpl
import com.example.awesomefamilyshoppinglist.utils.any
import com.example.awesomefamilyshoppinglist.utils.app
import com.example.awesomefamilyshoppinglist.utils.isGone
import com.example.awesomefamilyshoppinglist.utils.isVisible
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single
import it.cosenonjaviste.daggermock.DaggerMock
import it.cosenonjaviste.daggermock.DaggerMockRule
import it.cosenonjaviste.daggermock.InjectFromComponent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@SmallTest
@RunWith(AndroidJUnit4::class)
class SplashActivityTest {

    @get:Rule
    var ruleForDagger: DaggerMockRule<AppComponentForTest> = DaggerMock.rule<AppComponentForTest>(AppModuleForTest())
        .decorates(UserRepositoryImpl::class.java) { obj -> spy(obj) }
        .decorates(SplashRouterImpl::class.java) { obj -> spy(obj) }
        .set { component -> component.inject(app) }
        .customizeBuilder<AppComponentForTest.Builder> { it.provideApplication(app) }

    @get:Rule
    var ruleForLiveData = InstantTaskExecutorRule()

    @get:Rule
    val activityRule = ActivityTestRule<SplashActivity>(SplashActivity::class.java, false, false)

    @InjectFromComponent
    lateinit var userRepository: UserRepository

    @InjectFromComponent
    lateinit var splashRouter: SplashContract.Router

    @Test
    fun should_EnableTryAgainButton_When_SignInCancelledOrFailed() {
        Intents.init()
        `when`(userRepository.getCurrentUser()).thenReturn(Single.error(RuntimeException()))
        val activityResult = Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null)
        intending(hasComponent(userRepository.getSignInIntent().component)).respondWith(activityResult)
        activityRule.launchActivity(null)
        onView(withId(R.id.button)).isVisible()
        Intents.release()
    }

    @Test
    fun test_VisibilityOfViews_When_InitialState() {
        `when`(userRepository.getCurrentUser()).thenReturn(Single.error(RuntimeException()))
        doNothing().`when`(splashRouter).goToLogin(any())
        activityRule.launchActivity(null)
        onView(withId(R.id.button)).isGone()
        onView(withId(R.id.progressBar)).isGone()
        onView(withId(R.id.imageView)).check(matches(isDisplayed()))
    }

    @Test
    fun should_LaunchLogin_When_UserIsLoggedOut() {
        `when`(userRepository.getCurrentUser()).thenReturn(Single.error(RuntimeException()))
        doNothing().`when`(splashRouter).goToLogin(any())
        activityRule.launchActivity(null)
        verify(splashRouter).goToLogin(activityRule.activity)
    }

    @Test
    fun should_LaunchMain_When_UserIsLoggedIn() {
        `when`(userRepository.getCurrentUser()).thenReturn(Single.just(mock(FirebaseUser::class.java)))
        doNothing().`when`(splashRouter).goToMain(any())
        activityRule.launchActivity(null)
        verify(splashRouter).goToMain(activityRule.activity)
    }

    @Test
    fun should_LaunchMain_When_UserLogsIn() {
        Intents.init()
        `when`(userRepository.getCurrentUser()).thenReturn(Single.error(RuntimeException()))
        doNothing().`when`(splashRouter).goToMain(any())
        val activityResult = Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        intending(hasComponent(userRepository.getSignInIntent().component)).respondWith(activityResult)
        activityRule.launchActivity(null)
        verify(splashRouter).goToMain(activityRule.activity)
        Intents.release()
    }

    @Test
    fun should_LaunchSignIn_When_TryAgainButtonIsTapped() {
        Intents.init()
        val intentMatcher = hasComponent(userRepository.getSignInIntent().component)
        `when`(userRepository.getCurrentUser()).thenReturn(Single.error(RuntimeException()))
        val activityResult = Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null)
        intending(intentMatcher).respondWith(activityResult)
        activityRule.launchActivity(null)
        onView(withId(R.id.button)).perform(click())
        intended(intentMatcher, VerificationModes.times(2))
        Intents.release()
    }
}