package uk.ac.lshtm.mantra.android.scanning

import uk.ac.lshtm.mantra.core.Scanner

interface ScannerFactory {
    fun create(): Scanner
}
