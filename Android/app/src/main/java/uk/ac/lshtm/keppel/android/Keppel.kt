package uk.ac.lshtm.keppel.android

import android.app.Activity
import android.app.Application
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

interface Dependencies {
    val taskRunner: TaskRunner
    val scanners: List<ScannerFactory>
    val matcher: Matcher
}

fun Activity.dependencies(): Dependencies {
    return (this.application as Keppel).dependencies
}

class DefaultDependencies(
    override val taskRunner: TaskRunner = IODispatcherTaskRunner(),
    override val scanners: List<ScannerFactory> = listOf(
        BioMiniScannerFactory(),
        MFS100ScannerFactory(),
        DemoScannerFactory()
    ), override val matcher: Matcher = SourceAFISMatcher()
) : Dependencies

