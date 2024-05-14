package uk.ac.lshtm.keppel.android.support

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Root
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

object Assertions {

    fun assertTextDisplayed(text: String, root: Matcher<Root>? = null) {
        if (root != null) {
            onView(withText(text)).inRoot(root).check(matches(isDisplayed()))
        } else {
            onView(withText(text)).check(matches(isDisplayed()))
        }
    }

    fun assertTextDisplayed(text: Int, vararg formatArgs: Any, root: Matcher<Root>? = null) {
        val string =
            ApplicationProvider.getApplicationContext<Application>().getString(text, *formatArgs)
        assertTextDisplayed(string, root)
    }

    fun assertTextNotDisplayed(text: Int, vararg formatArgs: Any, root: Matcher<Root>? = null) {
        val string =
            ApplicationProvider.getApplicationContext<Application>().getString(text, *formatArgs)
        assertTextNotDisplayed(string, root)
    }

    fun assertTextNotDisplayed(text: String, root: Matcher<Root>? = null) {
        if (root != null) {
            onView(allOf(withText(text), isDisplayed())).inRoot(root).check(doesNotExist())
        } else {
            onView(allOf(withText(text), isDisplayed())).check(doesNotExist())
        }
    }
}
