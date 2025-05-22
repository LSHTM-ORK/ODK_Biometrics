package uk.ac.lshtm.keppel.aratek

import android.content.Context
import com.aratek.trustfinger.sdk.DeviceOpenListener
import com.aratek.trustfinger.sdk.FingerPosition
import com.aratek.trustfinger.sdk.ImgCompressAlg
import com.aratek.trustfinger.sdk.LedIndex
import com.aratek.trustfinger.sdk.LedStatus
import com.aratek.trustfinger.sdk.TrustFinger
import com.aratek.trustfinger.sdk.TrustFingerDevice
import com.aratek.trustfinger.sdk.TrustFingerException
import uk.ac.lshtm.keppel.core.CaptureResult
import uk.ac.lshtm.keppel.core.Scanner

class AratekScanner(private val context: Context) : Scanner {

    private var device: TrustFingerDevice? = null

    override fun connect(onConnected: (Boolean) -> Unit): Scanner {
        try {
            val trustFinger = TrustFinger.getInstance(context)
            trustFinger.initialize()
            trustFinger.openDevice(0, object : DeviceOpenListener {
                override fun openSuccess(trustFingerDevice: TrustFingerDevice) {
                    device = trustFingerDevice
                    onConnected(true)
                }

                override fun openFail(errorMessage: String) {
                    TODO("Not yet implemented")
                }
            })
        } catch (e: TrustFingerException) {
            throw RuntimeException("TrustFingerException: ${e.type}")
        }

        return this
    }

    override fun onDisconnect(onDisconnected: () -> Unit) {

    }

    override fun capture(): CaptureResult? {
        device!!.setLedStatus(LedIndex.RED, LedStatus.OPEN)

        var rawCapture: ByteArray
        do {
            rawCapture = device!!.captureRawData()
            val quality = device!!.rawDataQuality(rawCapture)
        } while (quality < 50)

        val isoData = device!!.rawToISO(
            rawCapture,
            device!!.imageInfo.width,
            device!!.imageInfo.height,
            device!!.imageInfo.resolution,
            FingerPosition.Unknown,
            ImgCompressAlg.UNCOMPRESSED_NO_BIT_PACKING
        )

        device!!.setLedStatus(LedIndex.RED, LedStatus.CLOSE)
        return CaptureResult(String(isoData), 0)
    }

    override fun stopCapture() {

    }

    override fun disconnect() {

    }
}
