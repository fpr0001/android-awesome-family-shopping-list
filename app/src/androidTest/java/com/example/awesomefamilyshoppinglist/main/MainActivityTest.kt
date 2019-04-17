package com.example.awesomefamilyshoppinglist.main

import android.view.Gravity
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
import com.example.awesomefamilyshoppinglist.utils.isGone
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    var mActivityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun clickOnLogoutNavigationItem_ShowsSplashScreen() {
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
            .perform(open()) // Open Drawer

        // Logout
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.action_logout))

        // Check that Splash Activity was eventually opened.
        onView(withContentDescription(R.string.splash_screen_try_again)).isGone()
    }

    @Test
    fun clickOnAndroidHomeIcon_OpensNavigation() {
        // Check that left drawer is closed at startup
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.

        // Open Drawer
        val navigateUpDesc = mActivityTestRule.activity
            .getString(R.string.navigation_drawer_open)
        onView(withContentDescription(navigateUpDesc)).perform(click())

        // Check if drawer is open
        onView(withId(R.id.drawer_layout))
            .check(matches(isOpen(Gravity.LEFT))) // Left drawer is open open.
    }

}