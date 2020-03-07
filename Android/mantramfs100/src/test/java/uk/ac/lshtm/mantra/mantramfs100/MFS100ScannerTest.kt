package uk.ac.lshtm.mantra.mantramfs100

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.mantra.mfs100.MFS100
import com.mantra.mfs100.MFS100Event
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MFS100ScannerTest {

    @Test
    fun setsApplicationContext() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        val mfs100 = mock(MFS100::class.java)

        MFS100Scanner(context) { mfs100 }
        verify(mfs100).SetApplicationContext(context)
    }

    @Test
    fun wheDeviceConnected_andVendorIDIsCorrect_andProductIsOld_andHasPermission_loadsFirmware() {
        VENDOR_IDS.forEach { vendorID ->
            val context = ApplicationProvider.getApplicationContext<Application>()
            val mfs100 = mock(MFS100::class.java)

            var mfs100Event: MFS100Event? = null
            MFS100Scanner(context) { event ->
                mfs100Event = event
                mfs100
            }

            mfs100Event!!.OnDeviceAttached(vendorID, OLD_DEVICE_ID, true)
            verify(mfs100).LoadFirmware()
        }
    }

    @Test
    fun wheDeviceConnected_andVendorIDIsCorrect_andProductIsUpToDate_andHasPermission_loadsFirmware() {
        VENDOR_IDS.forEach { vendorID ->
            val context = ApplicationProvider.getApplicationContext<Application>()
            val mfs100 = mock(MFS100::class.java)

            var mfs100Event: MFS100Event? = null
            MFS100Scanner(context) { event ->
                mfs100Event = event
                mfs100
            }

            mfs100Event!!.OnDeviceAttached(vendorID, NEW_DEVICE_ID, true)
            verify(mfs100).Init()
        }
    }
}

private val VENDOR_IDS = listOf(1204, 11279)
private const val OLD_DEVICE_ID = 34323
private const val NEW_DEVICE_ID = 4101