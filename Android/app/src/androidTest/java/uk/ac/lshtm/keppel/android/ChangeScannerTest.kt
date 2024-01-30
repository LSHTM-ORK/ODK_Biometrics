package uk.ac.lshtm.keppel.android

import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.lshtm.keppel.android.support.FakeScannerFactory
import uk.ac.lshtm.keppel.android.support.KeppelTestRule

@RunWith(AndroidJUnit4::class)
class ChangeScannerTest {

    @get:Rule
    val rule = KeppelTestRule(
        scanners = listOf(
            FakeScannerFactory(name = "Scanner 1"),
            FakeScannerFactory(name = "Scanner 2"),
            FakeScannerFactory(name = "Unavailable", isAvailable = false)
        )
    )

    @Test
    fun canChangeScanner() {
        rule.launchApp()

        onView(withText("Scanner 1")).check(matches(isDisplayed()))
        onView(withText(R.string.change_scanner)).perform(click())

        onView(withText("Scanner 2")).perform(click())
        onView(withText(R.string.app_name)).check(matches(isDisplayed()))

        val scannerPref =
            getDefaultSharedPreferences(getApplicationContext<Keppel>()).getString("scanner", null)
        assertThat(scannerPref, equalTo("Scanner 2"))
    }

    @Test
    fun unavailableScannersAreNotShown() {
        rule.launchApp()

        onView(withText("Scanner 1")).check(matches(isDisplayed()))
        onView(withText(R.string.change_scanner)).perform(click())

        onView(withText("Unavailable")).check(doesNotExist())
    }
}
