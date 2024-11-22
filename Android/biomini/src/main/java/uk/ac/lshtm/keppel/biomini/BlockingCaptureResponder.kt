package uk.ac.lshtm.keppel.biomini

import android.graphics.Bitmap
import com.suprema.CaptureResponder
import com.suprema.IBioMiniDevice
import java.util.concurrent.CountDownLatch

class BlockingCaptureResponder(private val device: IBioMiniDevice) : CaptureResponder() {

    private val latch = CountDownLatch(1)
    private var result: Pair<IBioMiniDevice.TemplateData, Int>? = null

    override fun onCapture(
        context: Any?,
        fingerState: IBioMiniDevice.FingerState?
    ) {
        super.onCapture(context, fingerState)
    }

    override fun onCaptureEx(
        context: Any?,
        option: IBioMiniDevice.CaptureOption,
        capturedImage: Bitmap?,
        capturedTemplate: IBioMiniDevice.TemplateData?,
        fingerState: IBioMiniDevice.FingerState?,
    ): Boolean {
        val imageData: ByteArray? = device.captureImageAsRAW_8
        val quality = if (imageData != null) {
            val mode: IBioMiniDevice.FpQualityMode =
                IBioMiniDevice.FpQualityMode.NQS_MODE_NFIQ
            device.getFPQuality(
                imageData,
                device.imageWidth,
                device.imageHeight,
                mode.value(),
            )
        } else {
            null
        }

        if (capturedTemplate != null && quality != null) {
            result = Pair(capturedTemplate, quality)
        }

        latch.countDown()
        return true
    }

    override fun onCaptureError(context: Any?, errorCode: Int, error: String) {
        latch.countDown()
    }

    fun awaitResult(): Pair<IBioMiniDevice.TemplateData, Int>? {
        latch.await()
        return result
    }
}
