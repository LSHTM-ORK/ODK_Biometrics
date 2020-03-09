package uk.ac.lshtm.mantra.mantramfs100

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.mantra.mfs100.MFS100
import com.mantra.mfs100.MFS100Event
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MFS100ScannerTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext<Application>()
    }

    @Test
    fun connect_setsApplicationContext() {
        val mfs100 = mock(MFS100::class.java)
        val mfsScanner = MFS100Scanner(context) { mfs100 }
        mfsScanner.connect {}
        verify(mfs100).SetApplicationContext(context)
    }

    @Test
    fun wheDeviceConnected_andVendorIDIsCorrect_andProductIsOld_andHasPermission_loadsFirmware() {
        VENDOR_IDS.forEach { vendorID ->
            val mfs100 = mock(MFS100::class.java)

            var mfs100Event: MFS100Event? = null
            val mfsScanner = MFS100Scanner(context) { event ->
                mfs100Event = event
                mfs100
            }

            mfsScanner.connect {}
            mfs100Event!!.OnDeviceAttached(vendorID, OLD_DEVICE_ID, true)

            verify(mfs100).LoadFirmware()
        }
    }

    @Test
    fun wheDeviceConnected_andVendorIDIsCorrect_andProductIsUpToDate_andHasPermission_loadsFirmware() {
        VENDOR_IDS.forEach { vendorID ->
            val mfs100 = mock(MFS100::class.java)

            var mfs100Event: MFS100Event? = null
            val mfsScanner = MFS100Scanner(context) { event ->
                mfs100Event = event
                mfs100
            }

            mfsScanner.connect {}
            mfs100Event!!.OnDeviceAttached(vendorID, NEW_DEVICE_ID, true)

            verify(mfs100).Init()
        }
    }

    @Test
    fun wheDeviceConnected_andVendorIDIsCorrect_andProductIsUpToDate_andHasPermission_onConnectedListenerIsCalled() {
        VENDOR_IDS.forEach { vendorID ->
            val mfs100 = mock(MFS100::class.java)

            var mfs100Event: MFS100Event? = null
            val mfSScanner = MFS100Scanner(context) { event ->
                mfs100Event = event
                mfs100
            }

            var listenerCalled = false
            mfSScanner.connect { listenerCalled = true }
            mfs100Event!!.OnDeviceAttached(vendorID, NEW_DEVICE_ID, true)

            assertThat(listenerCalled, equalTo(true))
        }
    }

    @Test
    fun wheDeviceConnected_andVendorIDIsCorrect_andProductIsUpToDate_andDoesNotHavePermission_onConnectedListenerNotCalled() {
        val mfs100 = mock(MFS100::class.java)
        var mfs100Event: MFS100Event? = null
        val mfSScanner = MFS100Scanner(context) { event ->
            mfs100Event = event
            mfs100
        }

        var listenerCalled = false
        mfSScanner.connect { listenerCalled = true }
        mfs100Event!!.OnDeviceAttached(VENDOR_IDS[1], NEW_DEVICE_ID, false)

        assertThat(listenerCalled, equalTo(false))
    }

    @Test
    fun disconnect_disposesMFS100() {
        val mfs100 = mock(MFS100::class.java)
        val mfSScanner = MFS100Scanner(context) { mfs100 }
        mfSScanner.disconnect()
        verify(mfs100).Dispose()
    }
}

private val VENDOR_IDS = listOf(1204, 11279) // We see 11279 for the MFS100
private const val OLD_DEVICE_ID = 34323
private const val NEW_DEVICE_ID = 4101 // This is the MFS100 product ID we see