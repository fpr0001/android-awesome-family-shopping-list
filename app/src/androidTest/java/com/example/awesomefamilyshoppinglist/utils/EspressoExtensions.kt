package com.example.awesomefamilyshoppinglist.utils

import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.example.awesomefamilyshoppinglist.App
import com.example.awesomefamilyshoppinglist.di.AppComponentForTest
import com.example.awesomefamilyshoppinglist.di.modules.AppModuleForTest
import it.cosenonjaviste.daggermock.DaggerMock

fun ViewInteraction.isGone() = getViewAssertion(ViewMatchers.Visibility.GONE)

fun ViewInteraction.isVisible() = getViewAssertion(ViewMatchers.Visibility.VISIBLE)

fun ViewInteraction.isInvisible() = getViewAssertion(ViewMatchers.Visibility.INVISIBLE)

private fun ViewInteraction.getViewAssertion(visibility: ViewMatchers.Visibility): ViewInteraction {
    return this.check(matches(ViewMatchers.withEffectiveVisibility(visibility)))
}

fun espressoDaggerMockRule() = DaggerMock.rule<AppComponentForTest>(AppModuleForTest()) {
    set { component -> component.inject(app) }
    customizeBuilder<AppComponentForTest.Builder> { it.provideApplication(app) }
}

val app: App
    get() = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as App