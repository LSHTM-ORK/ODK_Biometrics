package uk.ac.lshtm.keppel.aratek

import android.content.Context
import com.aratek.trustfinger.sdk.DeviceOpenListener
import com.aratek.trustfinger.sdk.FingerPosition
import com.aratek.trustfinger.sdk.LedIndex
import com.aratek.trustfinger.sdk.LedStatus
import com.aratek.trustfinger.sdk.TrustFinger
import com.aratek.trustfinger.sdk.TrustFingerDevice
import com.aratek.trustfinger.sdk.TrustFingerException
import uk.ac.lshtm.keppel.core.CaptureResult
import uk.ac.lshtm.keppel.core.Scanner
import uk.ac.lshtm.keppel.core.Scanner.Companion.TIMEOUT_MS
import uk.ac.lshtm.keppel.core.toHexString
import java.util.concurrent.atomic.AtomicBoolean

class AratekScanner(context: Context) : Scanner {

    private val trustFinger = TrustFinger.getInstance(context)
    private var device: AratekDeviceWrapper? = null

    override fun connect(onConnected: (Boolean) -> Unit): Scanner {
        try {
            trustFinger.initialize()
            trustFinger.openDevice(0, object : DeviceOpenListener {
                override fun openSuccess(trustFingerDevice: TrustFingerDevice) {
                    device = AratekDeviceWrapper(trustFingerDevice)
                    onConnected(true)
                }

                override fun openFail(errorMessage: String) {
                    onConnected(false)
                }
            })
        } catch (e: TrustFingerException) {
            onConnected(false)
        }

        return this
    }

    override fun onDisconnect(onDisconnected: () -> Unit) {
        // TODO
    }

    override fun capture(): CaptureResult? {
        return device?.let {
            val result = it.waitForTemplate()

            if (result != null) {
                CaptureResult(result.first.toHexString(), result.second)
            } else {
                null
            }
        }
    }

    override fun stopCapture() {
        device?.stopCapture()
    }

    override fun disconnect() {
        device?.close()
        trustFinger.release()
    }
}

/**
 * Wrapper for [TrustFingerDevice] that hides details like switching LEDs on/off when capturing
 */
private class AratekDeviceWrapper(val device: TrustFingerDevice) {

    private val capturing = AtomicBoolean(false)

    fun waitForTemplate(): Pair<ByteArray, Int>? {
        startCapture()

        val startTime = System.currentTimeMillis()
        var rawCapture: ByteArray
        do {
            if (!capturing.get() || (System.currentTimeMillis() - startTime) > TIMEOUT_MS) {
                return null
            }

            rawCapture = device.captureRawData()
            val quality = device.rawDataQuality(rawCapture)
        } while (quality < 50)

        stopCapture()
        return Pair(
            device.extractISOFeature(rawCapture, FingerPosition.Unknown),
            device.getNfiqScore(rawCapture)
        )
    }

    fun stopCapture() {
        device.setLedStatus(LedIndex.RED, LedStatus.CLOSE)
        capturing.set(false)
    }

    fun close() {
        device.close()
    }

    private fun startCapture() {
        device.setLedStatus(LedIndex.RED, LedStatus.OPEN)
        capturing.set(true)
    }
}
