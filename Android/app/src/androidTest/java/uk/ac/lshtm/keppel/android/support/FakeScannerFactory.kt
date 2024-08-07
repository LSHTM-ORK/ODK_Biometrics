package uk.ac.lshtm.keppel.android.support

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory
import uk.ac.lshtm.keppel.core.CaptureResult
import uk.ac.lshtm.keppel.core.Scanner
import uk.ac.lshtm.keppel.core.toHexString

class FakeScannerFactory(
    private val scanner: Scanner = FakeScanner(),
    override val isAvailable: Boolean = true,
    override val name: String = "Fake",
) : ScannerFactory {

    override fun create(context: Context): Scanner {
        return scanner
    }
}

class FakeScanner : Scanner {

    private var returnTemplate: String? = null
    private var returnNfiq: Int? = null
    private var fail = false

    private var capturing = false
    private var connected = false

    private var onConnected: () -> Unit = {}

    override fun connect(onConnected: () -> Unit): Scanner {
        this.onConnected = onConnected
        return this
    }

    override fun onDisconnect(onDisconnected: () -> Unit) {

    }

    override fun capture(): CaptureResult? {
        if (!connected) {
            throw IllegalStateException("Scanner not connected!")
        }

        capturing = true
        TaskRunnerIdlingResource.nonBlockingWait {
            capturing && returnTemplate == null && !fail
        }

        if (capturing) {
            capturing = false
            if (!fail) {
                val result = CaptureResult(returnTemplate!!.toHexString(), returnNfiq!!)
                returnTemplate = null
                return result
            } else {
                fail = false
                return null
            }
        } else {
            return null
        }
    }

    override fun stopCapture() {
        capturing = false
    }

    override fun disconnect() {
        connected = false
    }

    fun returnTemplate(template: String, nfiq: Int) {
        returnTemplate = template
        returnNfiq = nfiq
    }

    fun failToCapture() {
        fail = true
    }

    fun connect() {
        connected = true

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            onConnected()
        }
    }
}
