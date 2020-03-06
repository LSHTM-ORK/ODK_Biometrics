package uk.ac.lshtm.mantra.android

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.lshtm.mantra.android.settings.SettingsActivity

@RunWith(AndroidJUnit4::class)
class SettingsTest {

    @get:Rule
    val rule = ActivityScenarioRule(SettingsActivity::class.java)

    @Test
    fun canLaunchApp() {
        onView(withText(R.string.nothing_here_yet)).check(matches(isDisplayed()))
    }
}