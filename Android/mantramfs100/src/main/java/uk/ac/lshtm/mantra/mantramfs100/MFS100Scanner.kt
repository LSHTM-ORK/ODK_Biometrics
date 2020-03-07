package uk.ac.lshtm.mantra.mantramfs100

import android.content.Context
import com.mantra.mfs100.FingerData
import com.mantra.mfs100.MFS100
import com.mantra.mfs100.MFS100Event
import uk.ac.lshtm.mantra.core.Scanner

class MFS100Scanner(context: Context) : Scanner {

    private val mfS100 = MFS100(object : MFS100Event {
        override fun OnDeviceAttached(vid: Int, pid: Int, hasPermission: Boolean) {
            if (vid == 1204 || vid == 11279) {
                if (pid == 34323) {
                    loadFirmware()

                } else if (pid == 4101) {
                    initialize()
                }
            }
        }

        override fun OnDeviceDetached() {
        }

        override fun OnHostCheckFailed(p0: String?) {
        }
    })

    init {
        mfS100.SetApplicationContext(context)
        mfS100.Init()
    }

    override fun captureISOTemplate(): String {
        val fingerData = FingerData()
        mfS100.AutoCapture(fingerData, 10000, false)
        return fingerData.ISOTemplate().toHexString()
    }

    private fun initialize() {
        mfS100.Init()
    }

    private fun loadFirmware() {
        mfS100.LoadFirmware()
    }

    fun ByteArray.toHexString() = asUByteArray().joinToString("") { it.toString(16).padStart(2, '0') }
}