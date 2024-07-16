package uk.ac.lshtm.keppel.android.settings

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowToast
import uk.ac.lshtm.keppel.android.R

@RunWith(AndroidJUnit4::class)
class UsbDeviceAttachedActivityTest {

    private val application = ApplicationProvider.getApplicationContext<Application>()

    @Test
    fun `closes immediately`() {
        ActivityScenario.launch(UsbDeviceAttachedActivity::class.java).use {
            assertThat(it.state, equalTo(Lifecycle.State.DESTROYED))
        }
    }

    @Test
    fun `shows toast`() {
        ActivityScenario.launch(UsbDeviceAttachedActivity::class.java).use {
            val latestToast = ShadowToast.getTextOfLatestToast()
            assertThat(latestToast, equalTo(application.getString(R.string.scanner_connected)))
        }
    }
}
