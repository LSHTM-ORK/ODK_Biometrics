package uk.ac.lshtm.keppel.android

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.lshtm.keppel.android.settings.SettingsActivity

@RunWith(AndroidJUnit4::class)
class SettingsTest {

    @get:Rule
    val rule = ActivityTestRule<SettingsActivity>(SettingsActivity::class.java)

    @Test
    fun canLaunchApp() {
        onView(withText(R.string.nothing_here_yet)).check(matches(isDisplayed()))
    }
}