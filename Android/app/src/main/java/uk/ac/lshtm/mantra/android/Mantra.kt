package uk.ac.lshtm.mantra.android

import android.app.Activity
import android.app.Application
import android.content.Context
import uk.ac.lshtm.mantra.android.scanning.ScannerFactory
import uk.ac.lshtm.mantra.android.tasks.ThreadTaskRunner
import uk.ac.lshtm.mantra.core.Scanner
import uk.ac.lshtm.mantra.core.TaskRunner
import uk.ac.lshtm.mantra.mantramfs100.MFS100Scanner

class Mantra : Application() {

    var scannerFactory: ScannerFactory = object : ScannerFactory {
        override fun create(context: Context): Scanner {
            return MFS100Scanner(context)
        }
    }

    var taskRunner: TaskRunner = ThreadTaskRunner()
}

fun Activity.scannerFactory(): ScannerFactory {
    return (this.application as Mantra).scannerFactory
}

fun Activity.taskRunner(): TaskRunner {
    return (this.application as Mantra).taskRunner
}