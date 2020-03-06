package uk.ac.lshtm.mantra.android.scanning

import android.view.View
import kotlinx.android.synthetic.main.activity_scan.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class ScanActivityTest {

    @Test
    fun clickingCapture_showsProgressBar() {
        val activity = Robolectric.setupActivity(ScanActivity::class.java)

        activity.capture_button.performClick()
        assertThat(activity.capture_button.visibility, equalTo(View.GONE))
        assertThat(activity.progress_bar.visibility, equalTo(View.VISIBLE))
    }
}