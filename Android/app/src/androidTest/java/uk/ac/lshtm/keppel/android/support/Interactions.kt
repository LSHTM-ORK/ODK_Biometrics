package uk.ac.lshtm.keppel.android.support

import android.app.Application
import androidx.annotation.StringRes
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Root
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matcher

object Interactions {

    fun clickOn(text: String, root: Matcher<Root>? = null) {
        if (root != null) {
            onView(withText(text)).inRoot(root).perform(click())
        } else {
            onView(withText(text)).perform(click())
        }
    }

    fun clickOn(@StringRes text: Int, vararg formatArgs: Any, root: Matcher<Root>? = null) {
        val string =
            ApplicationProvider.getApplicationContext<Application>().getString(text, *formatArgs)
        clickOn(string)
    }
}
