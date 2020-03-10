package uk.ac.lshtm.keppel.android.scanning

import android.content.Context
import uk.ac.lshtm.keppel.core.Scanner

interface ScannerFactory {
    fun create(context: Context): Scanner
}
