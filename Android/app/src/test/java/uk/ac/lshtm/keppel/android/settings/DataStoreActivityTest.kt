package uk.ac.lshtm.keppel.android.settings

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.LooperMode
import org.robolectric.annotation.LooperMode.Mode.PAUSED
import uk.ac.lshtm.keppel.android.R

@RunWith(RobolectricTestRunner::class)
@LooperMode(PAUSED)
class DataStoreActivityTest {

    @get:Rule
    val rule = ActivityScenarioRule(SettingsActivity::class.java)

    @Test
    fun clickingOpenSourceLicenses_opensGoogleOpenSourceLicensesAcivity() {
        onView(withText(R.string.open_source_libraries)).perform(click())

        rule.scenario.onActivity { activity ->
            val expectedIntent = Intent(activity, OssLicensesMenuActivity::class.java)
            val actualIntent = shadowOf(activity).nextStartedActivity
            assertThat(actualIntent.component, equalTo(expectedIntent.component))
        }
    }
}
