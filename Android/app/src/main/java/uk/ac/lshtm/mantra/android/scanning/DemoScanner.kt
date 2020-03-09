package uk.ac.lshtm.mantra.android.scanning

import android.os.Handler
import uk.ac.lshtm.mantra.core.Scanner

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