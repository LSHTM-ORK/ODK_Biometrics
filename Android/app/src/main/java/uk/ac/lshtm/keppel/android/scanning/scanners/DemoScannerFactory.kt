package uk.ac.lshtm.keppel.android.scanning.scanners

import android.content.Context
import android.os.Handler
import android.os.Looper
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory
import uk.ac.lshtm.keppel.core.CaptureResult
import uk.ac.lshtm.keppel.core.Scanner
import java.util.concurrent.atomic.AtomicBoolean

class DemoScannerFactory : ScannerFactory {

    override val name: String = "Demo Scanner"
    override val isAvailable: Boolean = true

    override fun create(context: Context): Scanner =
        DemoScanner()
}

private class DemoScanner : Scanner {

    private var capturing = AtomicBoolean(false)

    override fun connect(onConnected: (Boolean) -> Unit): Scanner {
        Handler(Looper.getMainLooper()).postDelayed({ onConnected(true) }, 3000)
        return this
    }

    override fun onDisconnect(onDisconnected: () -> Unit) {

    }

    override fun capture(): CaptureResult? {
        capturing.set(true)
        Thread.sleep(3000)

        return if (capturing.getAndSet(false)) {
            CaptureResult("demo-finger-print-iso-template", 0)
        } else {
            null
        }
    }

    override fun stopCapture() {
        capturing.set(false)
    }

    override fun disconnect() {

    }
}
