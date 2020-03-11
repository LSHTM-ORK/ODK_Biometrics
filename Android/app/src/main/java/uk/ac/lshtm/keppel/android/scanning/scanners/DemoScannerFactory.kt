package uk.ac.lshtm.keppel.android.scanning.scanners

import android.content.Context
import android.os.Handler
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory
import uk.ac.lshtm.keppel.core.Scanner

class DemoScannerFactory : ScannerFactory {

    override val name: String = "Demo Scanner"

    override fun create(context: Context): Scanner =
        DemoScanner()
}

private class DemoScanner : Scanner {

    override fun connect(onConnected: () -> Unit) {
        Handler().postDelayed(onConnected, 3000)
    }

    override fun captureISOTemplate(): String {
        Thread.sleep(3000)
        return "demo-finger-print-iso-template"
    }

    override fun disconnect() {

    }
}
