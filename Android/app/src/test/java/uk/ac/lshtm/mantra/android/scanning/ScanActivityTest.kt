package uk.ac.lshtm.mantra.android.scanning

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.activity_scan.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.LooperMode
import uk.ac.lshtm.mantra.core.Scanner

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class ScanActivityTest {

    private val dummyScanner = DummyScanner()
    private lateinit var activity: ScanActivity

    @Before
    fun setup() {
        ScanActivity.SCANNER_FACTORY = DummyScannerFactory(dummyScanner)
        activity = Robolectric.setupActivity(ScanActivity::class.java)
    }

    @Test
    fun whenScannerDisconnected_showsConnectProgressBar() {
        assertThat(activity.capture_button.visibility, equalTo(View.GONE))
        assertThat(activity.capture_progress_bar.visibility, equalTo(View.GONE))
        assertThat(activity.connect_progress_bar.visibility, equalTo(View.VISIBLE))
    }

    @Test
    fun whenScannerConnected_clickingCapture_showsProgressBar() {
        dummyScanner.connect()

        activity.capture_button.performClick()
        assertThat(activity.capture_button.visibility, equalTo(View.GONE))
        assertThat(activity.capture_progress_bar.visibility, equalTo(View.VISIBLE))
        assertThat(activity.connect_progress_bar.visibility, equalTo(View.GONE))
    }
}

class DummyScannerFactory(private val dummyScanner: DummyScanner) : ScannerFactory {

    override fun create(context: Context): Scanner {
        return dummyScanner
    }
}

class DummyScanner : Scanner {

    private lateinit var onConnected: () -> Unit

    fun connect() {
       onConnected()
    }

    override fun connect(onConnected: () -> Unit) {
        this.onConnected = onConnected
    }

    override fun captureISOTemplate(): String {
        return ""
    }
}