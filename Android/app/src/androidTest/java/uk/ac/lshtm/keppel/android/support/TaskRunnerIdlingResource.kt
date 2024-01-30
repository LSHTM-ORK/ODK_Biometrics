package uk.ac.lshtm.keppel.android.support

import androidx.test.espresso.IdlingResource
import uk.ac.lshtm.keppel.core.TaskRunner

class TaskRunnerIdlingResource(val wrappedTaskRunner: TaskRunner) : TaskRunner, IdlingResource {

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
        return tasks == 0
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
