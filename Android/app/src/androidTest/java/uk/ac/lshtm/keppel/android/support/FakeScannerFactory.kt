package uk.ac.lshtm.keppel.android.support

import android.content.Context
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

class FakeScanner() : Scanner {

    private var returnTemplate: String? = null
    private var returnNfiq: Int? = null

    private var capturing = false

    override fun connect(onConnected: () -> Unit): Scanner {
        onConnected()
        return this
    }

    override fun onDisconnect(onDisconnected: () -> Unit) {

    }

    override fun capture(): CaptureResult? {
        capturing = true
        TaskRunnerIdlingResource.nonBlockingWait { capturing && returnTemplate == null }

        if (capturing) {
            capturing = false
            val result = CaptureResult(returnTemplate!!.toHexString(), returnNfiq!!)
            returnTemplate = null
            return result
        } else {
            return null
        }
    }

    override fun stopCapture() {
        capturing = false
    }

    override fun disconnect() {

    }

    fun returnTemplate(template: String, nfiq: Int) {
        returnTemplate = template
        returnNfiq = nfiq
    }
}
