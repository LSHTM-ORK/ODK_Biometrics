package uk.ac.lshtm.keppel.android.scanning.scanners

import android.content.Context
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory
import uk.ac.lshtm.keppel.core.Scanner

abstract class DynamicScannerFactory(private val className: String) : ScannerFactory {

    override val isAvailable: Boolean
        get() = loadClass(className) != null

    override fun create(context: Context): Scanner =
        loadClass(className)!!
            .getConstructor(Context::class.java)
            .newInstance(context) as Scanner

    private fun loadClass(className: String): Class<*>? {
        return try {
            Class.forName(className)
        } catch (e: ClassNotFoundException) {
            null
        }
    }
}
