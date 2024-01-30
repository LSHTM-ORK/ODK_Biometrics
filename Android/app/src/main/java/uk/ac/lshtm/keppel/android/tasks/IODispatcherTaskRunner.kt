package uk.ac.lshtm.keppel.android.tasks

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.lshtm.keppel.core.TaskRunner

class IODispatcherTaskRunner : TaskRunner {
    override fun execute(runnable: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch { runnable() }
    }
}
