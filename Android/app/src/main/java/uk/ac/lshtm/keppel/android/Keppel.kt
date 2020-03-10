package uk.ac.lshtm.keppel.android

import android.app.Activity
import android.app.Application
import android.content.Context
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory
import uk.ac.lshtm.keppel.android.tasks.ThreadTaskRunner
import uk.ac.lshtm.keppel.core.Scanner
import uk.ac.lshtm.keppel.core.TaskRunner
import uk.ac.lshtm.keppel.mantramfs100.MFS100Scanner

class Keppel : Application() {

    var scannerFactory: ScannerFactory = object : ScannerFactory {
        override fun create(context: Context): Scanner {
            return MFS100Scanner(context)
        }
    }

    var taskRunner: TaskRunner = ThreadTaskRunner()
}

fun Activity.scannerFactory(): ScannerFactory {
    return (this.application as Keppel).scannerFactory
}

fun Activity.taskRunner(): TaskRunner {
    return (this.application as Keppel).taskRunner
}