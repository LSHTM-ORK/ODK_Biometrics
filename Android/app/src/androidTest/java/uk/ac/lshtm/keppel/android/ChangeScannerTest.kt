package uk.ac.lshtm.keppel.android

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory
import uk.ac.lshtm.keppel.android.settings.SettingsActivity
import uk.ac.lshtm.keppel.core.Scanner

@RunWith(AndroidJUnit4::class)
class ChangeScannerTest {

    @get:Rule
    val rule = object : ActivityTestRule<SettingsActivity>(SettingsActivity::class.java) {
        override fun beforeActivityLaunched() {
            getApplicationContext<Keppel>().availableScanners = listOf(
                scannerFactory1,
                scannerFactory2
            )
            getApplicationContext<Keppel>().configureDefaultScanner(override = true)
        }
    }

    @Test
    fun clickingCapture_capturesPlacedFingerprintsISOTemplate_fromScanner() {
        onView(withText("Scanner 1")).check(matches(isDisplayed()))
        onView(withText(R.string.change_scanner)).perform(click())

        onView(withText("Scanner 2")).perform(click())
        onView(withText(R.string.app_name)).check(matches(isDisplayed()))

        val scannerPref =
            getDefaultSharedPreferences(getApplicationContext<Keppel>()).getString("scanner", null)
        assertThat(scannerPref, equalTo("Scanner 2"))
    }

    private val scannerFactory1 = object : ScannerFactory {

        override val name: String = "Scanner 1"

        override fun create(context: Context): Scanner {
            TODO("Not yet implemented")
        }
    }

    private val scannerFactory2 = object : ScannerFactory {

        override val name: String = "Scanner 2"

        override fun create(context: Context): Scanner {
            TODO("Not yet implemented")
        }
    }
}