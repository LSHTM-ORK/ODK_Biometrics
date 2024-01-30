package uk.ac.lshtm.keppel.android.scanning.scanners

import android.content.Context
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory
import uk.ac.lshtm.keppel.core.Scanner
import uk.ac.lshtm.keppel.biomini.BioMiniScanner

class BioMiniScannerFactory : ScannerFactory {

    override val name = "Suprema BioMini"

    override fun create(context: Context): Scanner = BioMiniScanner(context)
}