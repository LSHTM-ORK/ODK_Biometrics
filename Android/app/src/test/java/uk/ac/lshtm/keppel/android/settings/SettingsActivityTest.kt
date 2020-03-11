package uk.ac.lshtm.keppel.android.settings

import android.content.Intent
import android.view.View
import androidx.test.core.app.ActivityScenario
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import uk.ac.lshtm.keppel.android.R

@RunWith(RobolectricTestRunner::class)
class SettingsActivityTest {

    @Test
    fun clickingOpenSourceLicenses_opensGoogleOpenSourceLicensesAcivity() {
        val activityScenario = ActivityScenario.launch(SettingsActivity::class.java)

        activityScenario.onActivity { activity ->
            activity.findViewById<View>(R.id.open_source_licenses).performClick()

            val expectedIntent = Intent(activity, OssLicensesMenuActivity::class.java)
            val actualIntent = shadowOf(activity).nextStartedActivity
            assertThat(actualIntent.component, equalTo(expectedIntent.component))
        }
    }
}