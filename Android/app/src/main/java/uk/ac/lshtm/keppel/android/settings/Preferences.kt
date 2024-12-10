package uk.ac.lshtm.keppel.android.settings

import android.app.Activity
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory

object Preferences {
    const val SCANNER = "scanner"

    fun get(activity: Activity, scanners: List<ScannerFactory>): DataStore {
        return DataStore(
            getDefaultSharedPreferences(activity),
            mapOf(
                SCANNER to {
                    val availableScanners = scanners.filter { it.isAvailable }
                    availableScanners[0].name
                }
            )
        )
    }
}
