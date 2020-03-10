package uk.ac.lshtm.keppel.android.scanning

import android.os.Handler
import uk.ac.lshtm.keppel.core.Scanner

class DemoScanner : Scanner {

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