package uk.ac.lshtm.keppel.android.support

import android.content.Context
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory
import uk.ac.lshtm.keppel.core.CaptureResult
import uk.ac.lshtm.keppel.core.Scanner
import uk.ac.lshtm.keppel.core.toHexString

class FakeScannerFactory(
    private val scanner: Scanner = FakeScanner(),
    override val isAvailable: Boolean = true,
    override val name: String = "Fake"
) : ScannerFactory {

    override fun create(context: Context): Scanner {
        return scanner
    }
}

class FakeScanner : Scanner {

    var returnTemplate: String = "ISO TEMPLATE"
    var neverCapture = false

    private var capturing = false

    override fun connect(onConnected: () -> Unit): Scanner {
        onConnected()
        return this
    }

    override fun onDisconnect(onDisconnected: () -> Unit) {

    }

    override fun capture(): CaptureResult? {
        capturing = true

        val result = if (!neverCapture) {
            CaptureResult(returnTemplate.toHexString(), 17)
        } else {
            while (capturing) { Thread.sleep(1) }
            null
        }

        capturing = false
        return result
    }

    override fun stopCapture() {
        capturing = false
    }

    override fun disconnect() {

    }
}
