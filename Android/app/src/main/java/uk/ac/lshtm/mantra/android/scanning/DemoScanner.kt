package uk.ac.lshtm.mantra.android.scanning

import uk.ac.lshtm.mantra.core.Scanner

class DemoScanner : Scanner {

    override fun captureISOTemplate(): String {
        Thread.sleep(3000)
        return "demo-finger-print-iso-template"
    }
}