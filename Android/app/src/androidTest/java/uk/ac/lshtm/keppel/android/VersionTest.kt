package uk.ac.lshtm.keppel.android

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.lshtm.keppel.android.support.KeppelTestRule

@RunWith(AndroidJUnit4::class)
class VersionTest {

    @get:Rule
    val rule = KeppelTestRule()

    @Test
    fun canSeeAppVersion() {
        rule.launchApp()
        onView(withText(BuildConfig.VERSION_NAME)).check(matches(isDisplayed()))
    }
}
