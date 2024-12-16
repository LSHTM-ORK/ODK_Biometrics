package uk.ac.lshtm.keppel.android

import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.lshtm.keppel.android.settings.Preferences
import uk.ac.lshtm.keppel.android.support.FakeScanner
import uk.ac.lshtm.keppel.android.support.FakeScannerFactory
import uk.ac.lshtm.keppel.android.support.KeppelTestRule
import uk.ac.lshtm.keppel.android.support.pages.CapturePage
import uk.ac.lshtm.keppel.android.support.pages.DialogPage
import uk.ac.lshtm.keppel.android.support.pages.SettingsPage

@RunWith(AndroidJUnit4::class)
class ChangeScannerTest {

    private val scanner1 = FakeScanner()

    @get:Rule
    val rule = KeppelTestRule(
        scanners = listOf(
            FakeScannerFactory(name = "Scanner 1", scanner = scanner1),
            FakeScannerFactory(name = "Scanner 2"),
            FakeScannerFactory(name = "Unavailable", isAvailable = false)
        )
    )

    @Test
    fun canChangeScanner() {
        rule.launchApp()
            .assertTextDisplayed("Scanner 1")
            .clickChangeScanner()
            .changeScanner("Scanner 2")

        val scannerPref =
            getDefaultSharedPreferences(getApplicationContext<Keppel>()).getString(Preferences.SCANNER, null)
        assertThat(scannerPref, equalTo("Scanner 2"))
    }

    @Test
    fun canTestScanner() {
        rule.launchApp()
            .clickTestScanner()
            .connect(scanner1, CapturePage())
            .clickCapture()

        scanner1.returnTemplate("scanned", 0)
        DialogPage(R.string.scanner_test_success).assert()
            .clickOk(SettingsPage())
    }

    @Test
    fun unavailableScannersAreNotShown() {
        rule.launchApp()
            .clickChangeScanner()
            .assertNoScanner("Unavailable")
    }
}
