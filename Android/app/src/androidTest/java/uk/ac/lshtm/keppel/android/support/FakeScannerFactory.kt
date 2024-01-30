package uk.ac.lshtm.keppel.android.support

import android.content.Context
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory
import uk.ac.lshtm.keppel.core.CaptureResult
import uk.ac.lshtm.keppel.core.Scanner

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

    override fun connect(onConnected: () -> Unit): Scanner {
        onConnected()
        return this
    }

    override fun onDisconnect(onDisconnected: () -> Unit) {

    }

    override fun capture(): CaptureResult {
        return CaptureResult("ISO TEMPLATE", 17)
    }

    override fun stopCapture() {

    }

    override fun disconnect() {

    }
}
