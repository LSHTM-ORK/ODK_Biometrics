package uk.ac.lshtm.keppel.android.settings

import android.content.Context
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory

object Preferences {
    const val SCANNER = "scanner"

    fun get(context: Context, scanners: List<ScannerFactory>): DataStoreWithDefaults {
        return DataStoreWithDefaults(
            getDefaultSharedPreferences(context),
            mapOf(
                SCANNER to {
                    val availableScanners = scanners.filter { it.isAvailable }
                    availableScanners[0].name
                }
            )
        )
    }
}
