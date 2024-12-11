package uk.ac.lshtm.keppel.android.scanning

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.LooperMode
import uk.ac.lshtm.keppel.android.DefaultDependencies
import uk.ac.lshtm.keppel.android.External
import uk.ac.lshtm.keppel.android.Keppel
import uk.ac.lshtm.keppel.core.Scanner

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class ScanActivityTest {

    private val fakeScanner = mock<Scanner>()
    private lateinit var activity: ScanActivity
    private lateinit var activityController: ActivityController<ScanActivity>

    @Before
    fun setup() {
        ApplicationProvider.getApplicationContext<Keppel>().dependencies =
            DefaultDependencies(scanners = listOf(FakeScannerFactory(fakeScanner)))

        activityController = Robolectric.buildActivity(
            ScanActivity::class.java,
            Intent().also { it.action = External.ACTION_SCAN }
        )
        activity = activityController.setup().get()
    }

    @Test
    fun pausing_stopsCapture() {
        activityController.pause()
        verify(fakeScanner).stopCapture()
    }
}

class FakeScannerFactory(private val scanner: Scanner) : ScannerFactory {

    override val name: String = "Fake"
    override val isAvailable: Boolean = true

    override fun create(context: Context): Scanner = scanner
}
