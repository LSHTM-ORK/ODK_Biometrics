package uk.ac.lshtm.mantra.android.tasks

import uk.ac.lshtm.mantra.core.TaskRunner

class ThreadTaskRunner : TaskRunner {

    override fun execute(runnable: () -> Unit) {
        Thread(runnable).start()
    }
}