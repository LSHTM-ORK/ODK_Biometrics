package uk.ac.lshtm.keppel.android.support

import android.app.Activity
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import org.junit.rules.ExternalResource
import uk.ac.lshtm.keppel.android.Keppel
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory
import uk.ac.lshtm.keppel.android.settings.SettingsActivity
import uk.ac.lshtm.keppel.core.Matcher
import uk.ac.lshtm.keppel.core.TaskRunner

class KeppelTestRule(
    private val scanners: List<ScannerFactory> = listOf(FakeScannerFactory()),
    private val matcher: Matcher = FakeMatcher()
) : ExternalResource() {

    var waitForBackgroundTasks = true
        set (value) {
            field = value
            taskRunnerIdlingResource.enabled = value
        }

    private val application = ApplicationProvider.getApplicationContext<Keppel>()
    private val taskRunnerIdlingResource = TaskRunnerIdlingResource(application.taskRunner)

    private var activityScenario: ActivityScenario<*>? = null
        set (value) {
            if (field != null) {
                throw IllegalStateException("ActivityScenario already launched!")
            }

            field = value
        }

    override fun before() {
        application.setDependencies(
            availableScanners = scanners,
            matcher = matcher,
            taskRunner = taskRunnerIdlingResource
        )
        application.configureDefaultScanner(true)

        IdlingRegistry.getInstance().register(taskRunnerIdlingResource)
    }

    override fun after() {
        IdlingRegistry.getInstance().unregister(taskRunnerIdlingResource)
    }

    fun launchApp() {
        ActivityScenario.launch(SettingsActivity::class.java).also {
            activityScenario = it
        }
    }

    fun launchAction(intent: Intent): ActivityScenario<Activity> {
        return ActivityScenario.launchActivityForResult<Activity>(intent).also {
            activityScenario = it
        }
    }
}

private class TaskRunnerIdlingResource(val wrappedTaskRunner: TaskRunner) : TaskRunner,
    IdlingResource {

    var enabled = true

    private var tasks = 0
    private var idleTransitionCallback: IdlingResource.ResourceCallback? = null

    override fun execute(runnable: () -> Unit) {
        increment()
        wrappedTaskRunner.execute {
            runnable()
            decrement()
        }
    }

    override fun getName(): String {
        return TaskRunnerIdlingResource::class.java.name
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.idleTransitionCallback = callback
    }

    override fun isIdleNow(): Boolean {
        return if (enabled) {
            tasks == 0
        } else {
            true
        }
    }

    private fun increment() {
        synchronized(this) {
            tasks += 1
        }
    }

    private fun decrement() {
        synchronized(this) {
            tasks -= 1

            if (isIdleNow) {
                idleTransitionCallback?.onTransitionToIdle()
            }
        }
    }
}
