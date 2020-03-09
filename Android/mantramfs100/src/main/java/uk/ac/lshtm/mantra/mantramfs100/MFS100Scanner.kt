package uk.ac.lshtm.mantra.mantramfs100

import android.content.Context
import com.mantra.mfs100.FingerData
import com.mantra.mfs100.MFS100
import com.mantra.mfs100.MFS100Event
import uk.ac.lshtm.mantra.core.Scanner

class MFS100Scanner(private val context: Context, mfs100Provider: (MFS100Event) -> MFS100) : Scanner {

    constructor(context: Context) : this(context, ::MFS100)

    private lateinit var onConnected: () -> Unit

    private val mfS100 = mfs100Provider(object : MFS100Event {
        override fun OnDeviceAttached(vendorID: Int, productID: Int, hasPermission: Boolean) {
            if (!hasPermission) {
                return
            }

            if (vendorID == 1204 || vendorID == 11279) {
                if (productID == 34323) {
                    loadFirmware()

                } else if (productID == 4101) {
                    initialize()
                }
            }
        }

        override fun OnDeviceDetached() {
        }

        override fun OnHostCheckFailed(p0: String?) {
        }
    })

    override fun connect(onConnected: () -> Unit) {
        this.onConnected = onConnected
        mfS100.SetApplicationContext(context)
    }

    override fun captureISOTemplate(): String {
        val fingerData = FingerData()
        mfS100.AutoCapture(fingerData, 10000, false)
        return fingerData.ISOTemplate().toHexString()
    }

    override fun disconnect() {
        mfS100.Dispose()
    }

    private fun initialize() {
        mfS100.Init()
        onConnected()
    }

    private fun loadFirmware() {
        mfS100.LoadFirmware()
    }

    @kotlin.ExperimentalUnsignedTypes
    private fun ByteArray.toHexString() = asUByteArray().joinToString("") { it.toString(16).padStart(2, '0') }
}