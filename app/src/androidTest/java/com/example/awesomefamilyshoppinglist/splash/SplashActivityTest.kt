package com.example.awesomefamilyshoppinglist.splash

import android.app.Activity
import android.app.Instrumentation
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.VerificationModes
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.rule.ActivityTestRule
import com.example.awesomefamilyshoppinglist.R
import com.example.awesomefamilyshoppinglist.di.AppComponentForTest
import com.example.awesomefamilyshoppinglist.di.modules.AppModuleForTest
import com.example.awesomefamilyshoppinglist.utils.any
import com.example.awesomefamilyshoppinglist.utils.app
import com.example.awesomefamilyshoppinglist.utils.isGone
import com.example.awesomefamilyshoppinglist.utils.isVisible
import io.reactivex.Completable
import it.cosenonjaviste.daggermock.DaggerMock
import it.cosenonjaviste.daggermock.DaggerMockRule
import it.cosenonjaviste.daggermock.InjectFromComponent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.Spy

@SmallTest
@RunWith(AndroidJUnit4::class)
class SplashActivityTest {

    @get:Rule
    var ruleForDagger: DaggerMockRule<AppComponentForTest> = DaggerMock.rule<AppComponentForTest>(AppModuleForTest())
        .decorates(SplashRouterImpl::class.java) { obj -> spy(obj) }
        .set { component -> component.inject(app) }
        .customizeBuilder<AppComponentForTest.Builder> { it.provideApplication(app) }

    @get:Rule
    var ruleForLiveData = InstantTaskExecutorRule()

    @get:Rule
    val activityRule = ActivityTestRule(SplashActivity::class.java, false, false)

    @Spy
    lateinit var splashUseCases: SplashContract.SplashUseCases

    @InjectFromComponent
    lateinit var splashRouter: SplashContract.Router

    @Test
    fun should_EnableTryAgainButton_When_SignInCancelledOrFailed() {
        Intents.init()
        `when`(splashUseCases.fetchAndStoreEntities(any())).thenReturn(Completable.error(RuntimeException()))
        val activityResult = Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null)
        intending(hasComponent(splashRouter.getSignInIntent().component)).respondWith(activityResult)
        activityRule.launchActivity(null)
        onView(withId(R.id.button)).isVisible()
        Intents.release()
    }

    @Test
    fun test_VisibilityOfViews_When_InitialState() {
        `when`(splashUseCases.fetchAndStoreEntities(any())).thenReturn(Completable.error(RuntimeException()))
        doNothing().`when`(splashRouter).goToLogin(any())
        activityRule.launchActivity(null)
        onView(withId(R.id.button)).isGone()
        onView(withId(R.id.progressBar)).isGone()
    }

    @Test
    fun should_LaunchLogin_When_UserIsLoggedOut() {
        `when`(splashUseCases.fetchAndStoreEntities(any())).thenReturn(Completable.error(SplashContract.Exception(SplashContract.Status.StatusLoggedOut)))
        doNothing().`when`(splashRouter).goToLogin(any())
        activityRule.launchActivity(null)
        verify(splashRouter).goToLogin(activityRule.activity)
    }

    @Test
    fun should_LaunchMain_When_UserIsLoggedIn() {
        `when`(splashUseCases.fetchAndStoreEntities(any())).thenReturn(Completable.complete())
        doNothing().`when`(splashRouter).goToMain(any())
        activityRule.launchActivity(null)
        verify(splashRouter).goToMain(activityRule.activity)
    }

    @Test
    fun should_LaunchMain_When_UserLogsIn() {
        `when`(splashUseCases.fetchAndStoreEntities(any())).thenReturn(Completable.error(SplashContract.Exception(SplashContract.Status.StatusLoggedOut)))
            .thenReturn(Completable.complete())
        doNothing().`when`(splashRouter).goToMain(any())
        val activityResult = Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        Intents.init()
        intending(hasComponent(splashRouter.getSignInIntent().component)).respondWith(activityResult)
        activityRule.launchActivity(null)
        verify(splashRouter).goToMain(activityRule.activity)
        Intents.release()
    }

    @Test
    fun should_LaunchSignIn_When_TryAgainButtonIsTapped() {
        Intents.init()
        val intentMatcher = hasComponent(splashRouter.getSignInIntent().component)
        `when`(splashUseCases.fetchAndStoreEntities(any())).thenReturn(Completable.error(SplashContract.Exception(SplashContract.Status.StatusLoggedOut)))
        val activityResult = Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null)
        intending(intentMatcher).respondWith(activityResult)
        activityRule.launchActivity(null)
        onView(withId(R.id.button)).perform(click())
        intended(intentMatcher, VerificationModes.times(2))
        Intents.release()
    }
}