package uk.ac.lshtm.keppel.android

import android.app.Activity
import android.app.Application
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory
import uk.ac.lshtm.keppel.android.scanning.scanners.BioMiniScannerFactory
import uk.ac.lshtm.keppel.android.scanning.scanners.DemoScannerFactory
import uk.ac.lshtm.keppel.android.scanning.scanners.MFS100ScannerFactory
import uk.ac.lshtm.keppel.android.tasks.IODispatcherTaskRunner
import uk.ac.lshtm.keppel.core.TaskRunner

class Keppel : Application() {

    var taskRunner: TaskRunner = IODispatcherTaskRunner()

    var availableScanners: List<ScannerFactory> = listOf(
        BioMiniScannerFactory(),
        MFS100ScannerFactory(),
        DemoScannerFactory()
    )

    val scannerFactory: ScannerFactory
        get() {
            val sharedPreferences = getDefaultSharedPreferences(this)
            return availableScanners.first {
                it.name == sharedPreferences.getString("scanner", null)
            }
        }

    override fun onCreate() {
        super.onCreate()
        configureDefaultScanner(false)
    }

    fun configureDefaultScanner(override: Boolean) {
        availableScanners = availableScanners.filter { it.isAvailable }

        val sharedPreference = getDefaultSharedPreferences(this)
        if (override || !sharedPreference.contains("scanner")) {
            sharedPreference.edit {
                putString("scanner", availableScanners[0].name)
            }
        }
    }
}

fun Activity.scannerFactory(): ScannerFactory {
    return (this.application as Keppel).scannerFactory
}

fun Activity.taskRunner(): TaskRunner {
    return (this.application as Keppel).taskRunner
}

fun Fragment.availableScanners(): List<ScannerFactory> {
    return (this.requireActivity().application as Keppel).availableScanners
}
