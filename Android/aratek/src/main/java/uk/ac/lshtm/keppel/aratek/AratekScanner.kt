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
    private var device: TrustFingerDevice? = null

    private var capturing = AtomicBoolean(false)

    override fun connect(onConnected: (Boolean) -> Unit): Scanner {
        try {
            trustFinger.initialize()
            trustFinger.openDevice(0, object : DeviceOpenListener {
                override fun openSuccess(trustFingerDevice: TrustFingerDevice) {
                    device = trustFingerDevice
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

    }

    override fun capture(): CaptureResult? {
        return device?.let {
            it.setLedStatus(LedIndex.RED, LedStatus.OPEN)

            val startTime = System.currentTimeMillis()
            capturing.set(true)
            var rawCapture: ByteArray
            do {
                if (!capturing.get() || (System.currentTimeMillis() - startTime) > TIMEOUT_MS) {
                    return null
                }

                rawCapture = it.captureRawData()
                val quality = it.rawDataQuality(rawCapture)
            } while (quality < 50)
            capturing.set(false)

            val isoData = it.extractISOFeature(rawCapture, FingerPosition.Unknown)
            it.setLedStatus(LedIndex.RED, LedStatus.CLOSE)
            return CaptureResult(isoData.toHexString(), it.getNfiqScore(rawCapture))
        }
    }

    override fun stopCapture() {
        device?.setLedStatus(LedIndex.RED, LedStatus.CLOSE)
        capturing.set(false)
    }

    override fun disconnect() {
        device?.close()
        trustFinger.release()
    }
}
