package uk.ac.lshtm.keppel.android

import android.app.Activity
import android.app.Application
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory
import uk.ac.lshtm.keppel.core.Analytics
import uk.ac.lshtm.keppel.core.Matcher
import uk.ac.lshtm.keppel.core.TaskRunner

class Keppel : Application() {

    var dependencies: Dependencies = DefaultDependencies()

    override fun onCreate() {
        super.onCreate()
        configureAnalytics()
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

