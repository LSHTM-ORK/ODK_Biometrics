package uk.ac.lshtm.mantra.android

import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.lshtm.mantra.android.scanning.ScanActivity
import uk.ac.lshtm.mantra.android.scanning.ScannerFactory
import uk.ac.lshtm.mantra.core.Scanner

@RunWith(AndroidJUnit4::class)
class ScanFingerTest {

    private val dummyScanner = DummyScanner("finger-data")

    @get:Rule
    val rule = object : ActivityTestRule<ScanActivity>(ScanActivity::class.java) {
        override fun beforeActivityLaunched() {
            ScanActivity.SCANNER_FACTORY = DummyScannerFactory(dummyScanner)
        }
    }

    @Test
    fun clickingCapture_capturesPlacedFingerprintsISOTemplate_fromScanner() {
        onView(withText(R.string.capture)).perform(click())
        assertThat(rule.activityResult.resultCode, equalTo(Activity.RESULT_OK));
        assertThat(rule.activityResult.resultData.extras!!.getString("value"), equalTo("ISO TEMPLATE finger-data"))
    }
}

class DummyScannerFactory(private val dummyScanner: DummyScanner) : ScannerFactory {

    override fun create(): Scanner {
        return dummyScanner
    }
}

class DummyScanner(private val fingerData: String) : Scanner {

    override fun captureISOTemplate(): String {
        return "ISO TEMPLATE $fingerData"
    }
}
