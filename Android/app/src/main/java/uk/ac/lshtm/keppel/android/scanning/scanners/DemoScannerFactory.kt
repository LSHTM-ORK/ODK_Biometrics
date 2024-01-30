package uk.ac.lshtm.keppel.android.scanning.scanners

import android.content.Context
import android.os.Handler
import android.os.Looper
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory
import uk.ac.lshtm.keppel.core.Scanner

class DemoScannerFactory : ScannerFactory {

    override val name: String = "Demo Scanner"
    override val isAvailable: Boolean = true

    override fun create(context: Context): Scanner =
        DemoScanner()
}

private class DemoScanner : Scanner {

    override fun connect(onConnected: () -> Unit): Scanner {
        Handler(Looper.getMainLooper()).postDelayed(onConnected, 3000)
        return this
    }

    override fun onDisconnect(onDisconnected: () -> Unit) {

    }

    override fun captureISOTemplate(): String {
        Thread.sleep(3000)
        return "demo-finger-print-iso-template"
    }

    override fun stopCapture() {

    }

    override fun disconnect() {

    }
}
