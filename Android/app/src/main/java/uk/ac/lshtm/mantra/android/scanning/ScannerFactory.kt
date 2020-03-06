package uk.ac.lshtm.mantra.android.scanning

import android.content.Context
import uk.ac.lshtm.mantra.core.Scanner

interface ScannerFactory {
    fun create(context: Context): Scanner
}
