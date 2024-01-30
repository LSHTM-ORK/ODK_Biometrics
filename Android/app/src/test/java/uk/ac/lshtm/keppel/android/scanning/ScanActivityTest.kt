package uk.ac.lshtm.keppel.android.scanning

import android.content.Context
import android.view.View
import androidx.test.core.app.ApplicationProvider
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.LooperMode
import uk.ac.lshtm.keppel.android.Keppel
import uk.ac.lshtm.keppel.core.Scanner
import uk.ac.lshtm.keppel.android.R

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class ScanActivityTest {

    private val fakeScanner = spy(FakeScanner())
    private lateinit var activity: ScanActivity
    private lateinit var activityController: ActivityController<ScanActivity>

    @Before
    fun setup() {
        ApplicationProvider.getApplicationContext<Keppel>().availableScanners =
            listOf(FakeScannerFactory(fakeScanner))
        ApplicationProvider.getApplicationContext<Keppel>().configureDefaultScanner(override = true)
        activityController = Robolectric.buildActivity(ScanActivity::class.java)
        activity = activityController.setup().get()
    }

    @Test
    fun whenScannerDisconnected_showsConnectProgressBar() {
        assertThat(activity.findViewById<View>(R.id.capture_button).visibility, equalTo(View.GONE))
        assertThat(activity.findViewById<View>(R.id.capture_progress_bar).visibility, equalTo(View.GONE))
        assertThat(activity.findViewById<View>(R.id.connect_progress_bar).visibility, equalTo(View.VISIBLE))
    }

    @Test
    fun whenScannerConnected_clickingCapture_showsProgressBar() {
        fakeScanner.connect()

        activity.findViewById<View>(R.id.capture_button).performClick()
        assertThat(activity.findViewById<View>(R.id.capture_button).visibility, equalTo(View.GONE))
        assertThat(activity.findViewById<View>(R.id.capture_progress_bar).visibility, equalTo(View.VISIBLE))
        assertThat(activity.findViewById<View>(R.id.connect_progress_bar).visibility, equalTo(View.GONE))
    }

    @Test
    fun whenScannerConnected_thenDisconnected_showsConnectProgressBar() {
        fakeScanner.connect()
        fakeScanner.disconnect()

        assertThat(activity.findViewById<View>(R.id.capture_button).visibility, equalTo(View.GONE))
        assertThat(activity.findViewById<View>(R.id.capture_progress_bar).visibility, equalTo(View.GONE))
        assertThat(activity.findViewById<View>(R.id.connect_progress_bar).visibility, equalTo(View.VISIBLE))
    }

    @Test
    fun pausing_stopsCapture() {
        activityController.pause()
        verify(fakeScanner).stopCapture()
    }
}

class FakeScannerFactory(private val fakeScanner: FakeScanner) : ScannerFactory {

    override val name: String = "Fake"
    override val isAvailable: Boolean = true

    override fun create(context: Context): Scanner = fakeScanner
}

open class FakeScanner : Scanner {

    private var onDisconnected: (() -> Unit)? = null
    private lateinit var onConnected: () -> Unit

    fun connect() {
        onConnected()
    }

    override fun connect(onConnected: () -> Unit): Scanner {
        this.onConnected = onConnected
        return this
    }

    override fun onDisconnect(onDisconnected: () -> Unit) {
        this.onDisconnected = onDisconnected
    }

    override fun captureISOTemplate(): String {
        return ""
    }

    override fun stopCapture() {

    }

    override fun disconnect() {
        onDisconnected?.invoke()
    }
}
