package uk.ac.lshtm.mantra.android.scanning

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.activity_scan.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.LooperMode
import uk.ac.lshtm.mantra.core.Scanner

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class ScanActivityTest {

    @Test
    fun clickingCapture_showsProgressBar() {
        ScanActivity.SCANNER_FACTORY = DummyScannerFactory()
        val activity = Robolectric.setupActivity(ScanActivity::class.java)

        activity.capture_button.performClick()
        assertThat(activity.capture_button.visibility, equalTo(View.GONE))
        assertThat(activity.progress_bar.visibility, equalTo(View.VISIBLE))
    }
}

class DummyScannerFactory : ScannerFactory {

    override fun create(context: Context): Scanner {
        return DummyScanner()
    }
}

class DummyScanner : Scanner {

    override fun captureISOTemplate(): String {
        return ""
    }
}