package uk.ac.lshtm.keppel.android.scanning.scanners

import android.content.Context
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory
import uk.ac.lshtm.keppel.core.Scanner
import uk.ac.lshtm.keppel.mantramfs100.MFS100Scanner

class MFS100ScannerFactory : ScannerFactory {

    override val name = "Mantra MFS100"

    override fun create(context: Context): Scanner = MFS100Scanner(context)
}