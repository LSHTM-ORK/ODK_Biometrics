package uk.ac.lshtm.keppel.android.support

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import org.junit.rules.ExternalResource
import uk.ac.lshtm.keppel.android.Keppel
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory
import uk.ac.lshtm.keppel.android.settings.SettingsActivity
import uk.ac.lshtm.keppel.android.support.pages.Page
import uk.ac.lshtm.keppel.android.support.pages.SettingsPage
import uk.ac.lshtm.keppel.core.Matcher
import uk.ac.lshtm.keppel.core.TaskRunner

class KeppelTestRule(
    private val scanners: List<ScannerFactory> = listOf(FakeScannerFactory()),
    private val matcher: Matcher = FakeMatcher()
) : ExternalResource() {

    private val application = ApplicationProvider.getApplicationContext<Keppel>()
    private val taskRunnerIdlingResource = TaskRunnerIdlingResource(application.taskRunner)

    private var activityScenario: ActivityScenario<*>? = null
        set(value) {
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

    fun launchApp(): SettingsPage {
        ActivityScenario.launch(SettingsActivity::class.java).also {
            activityScenario = it
        }

        return SettingsPage().assert()
    }

    fun <T : Page<T>> launchAction(
        intent: Intent,
        page: T,
        block: (T) -> Unit
    ): Instrumentation.ActivityResult {
        val scenario = launchActivityScenario(intent)
        block(page.assert())
        return scenario.result
    }

    private fun launchActivityScenario(intent: Intent): ActivityScenario<Activity> {
        return ActivityScenario.launchActivityForResult<Activity>(intent).also {
            activityScenario = it
        }
    }
}

class TaskRunnerIdlingResource(val wrappedTaskRunner: TaskRunner) : TaskRunner,
    IdlingResource {

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
        return waiting || tasks == 0
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

    companion object {
        private var waiting = false

        /**
         * Wait for condition to be `true` without blocking tests by temporarily disabling
         * the idling resource checks on running tasks. This allows fake components being called
         * in the background to signal that the UI can be interacted with/asserted on until some
         * condition is met (like some fake state is setup).
         */
        fun nonBlockingWait(condition: () -> Boolean) {
            while (condition()) {
                waiting = true
                Thread.sleep(1)
            }

            waiting = false
        }
    }
}
