package uk.ac.lshtm.keppel.android

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceDataStore
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import uk.ac.lshtm.keppel.android.matching.SourceAFISMatcher
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory
import uk.ac.lshtm.keppel.android.scanning.scanners.BioMiniScannerFactory
import uk.ac.lshtm.keppel.android.scanning.scanners.DemoScannerFactory
import uk.ac.lshtm.keppel.android.scanning.scanners.MFS100ScannerFactory
import uk.ac.lshtm.keppel.android.tasks.IODispatcherTaskRunner
import uk.ac.lshtm.keppel.core.Analytics
import uk.ac.lshtm.keppel.core.Matcher
import uk.ac.lshtm.keppel.core.TaskRunner

class Keppel : Application() {

    var dependencies: Dependencies = DefaultDependencies()
        private set

    override fun onCreate() {
        super.onCreate()
        configureAnalytics()
    }

    fun setDependencies(dependencies: Dependencies) {
        this.dependencies = dependencies
    }

    private fun configureAnalytics() {
        if (BuildConfig.ANALYTICS_CLASS != null) {
            val analyticsClass = Class.forName(BuildConfig.ANALYTICS_CLASS)
            val instance =
                analyticsClass.getConstructor(Application::class.java)
                    .newInstance(this) as Analytics
            Analytics.setInstance(instance)
        }
    }
}

fun Activity.dependencies(): Dependencies {
    return (this.application as Keppel).dependencies
}

fun Activity.settings(): Settings {
    return Settings(
        getDefaultSharedPreferences(this),
        mapOf(
            "scanner" to {
                val availableScanners = dependencies().scanners.filter { it.isAvailable }
                availableScanners[0].name
            }
        )
    )
}

fun Activity.scannerFactory(): ScannerFactory {
    val settings = settings()
    return dependencies().scanners.first {
        it.name == settings.getString("scanner", null)
    }
}

interface Dependencies {
    val taskRunner: TaskRunner
    val scanners: List<ScannerFactory>
    val matcher: Matcher
}

class DefaultDependencies(
    override val taskRunner: TaskRunner = IODispatcherTaskRunner(),
    override val scanners: List<ScannerFactory> = listOf(
        BioMiniScannerFactory(),
        MFS100ScannerFactory(),
        DemoScannerFactory()
    ), override val matcher: Matcher = SourceAFISMatcher()
) : Dependencies

class Settings(
    private val sharedPreferences: SharedPreferences,
    private val defaults: Map<String?, () -> String>
) : PreferenceDataStore() {
    override fun getString(key: String?, defValue: String?): String? {
        return if (sharedPreferences.contains(key)) {
            return sharedPreferences.getString(key, defValue)
        } else {
            defaults[key]?.invoke() ?: defValue
        }
    }

    override fun putString(key: String?, value: String?) {
       sharedPreferences.edit()
           .putString(key, value)
           .apply()
    }
}
