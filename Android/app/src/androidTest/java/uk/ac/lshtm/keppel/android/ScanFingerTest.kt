package uk.ac.lshtm.keppel.android

import android.app.Activity
import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
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
import uk.ac.lshtm.keppel.android.scanning.ScanActivity
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory
import uk.ac.lshtm.keppel.core.Scanner

@RunWith(AndroidJUnit4::class)
class ScanFingerTest {

    private val fakeScanner = FakeScanner("finger-data")
    private val fakeScannerFactory = FakeScannerFactory(fakeScanner)

    @get:Rule
    val rule = object : ActivityTestRule<ScanActivity>(ScanActivity::class.java) {
        override fun beforeActivityLaunched() {
            getApplicationContext<Keppel>().availableScanners = listOf(fakeScannerFactory)
            getApplicationContext<Keppel>().configureDefaultScanner(override = true)
        }
    }

    @Test
    fun clickingCapture_capturesPlacedFingerprintsISOTemplate_fromScanner() {
        onView(withText(R.string.capture)).perform(click())
        assertThat(rule.activityResult.resultCode, equalTo(Activity.RESULT_OK));
        assertThat(rule.activityResult.resultData.extras!!.getString("value"), equalTo("ISO TEMPLATE finger-data"))
    }
}

class FakeScannerFactory(private val fakeScanner: FakeScanner) : ScannerFactory {

    override val name = "Fake"

    override fun create(context: Context): Scanner {
        return fakeScanner
    }
}

class FakeScanner(private val fingerData: String) : Scanner {

    override fun connect(onConnected: () -> Unit): Scanner {
        onConnected()
        return this
    }

    override fun onDisconnect(onDisconnected: () -> Unit) {

    }

    override fun captureISOTemplate(): String {
        return "ISO TEMPLATE $fingerData"
    }

    override fun disconnect() {

    }
}
