package uk.ac.lshtm.keppel.android

import uk.ac.lshtm.keppel.android.matching.SourceAFISMatcher
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory
import uk.ac.lshtm.keppel.android.scanning.scanners.AratekScannerFactory
import uk.ac.lshtm.keppel.android.scanning.scanners.BioMiniScannerFactory
import uk.ac.lshtm.keppel.android.scanning.scanners.DemoScannerFactory
import uk.ac.lshtm.keppel.android.scanning.scanners.MFS100ScannerFactory
import uk.ac.lshtm.keppel.android.tasks.IODispatcherTaskRunner
import uk.ac.lshtm.keppel.core.Matcher
import uk.ac.lshtm.keppel.core.TaskRunner

class DefaultDependencies(
    override val taskRunner: TaskRunner = IODispatcherTaskRunner(),
    override val scanners: List<ScannerFactory> = listOf(
        BioMiniScannerFactory(),
        MFS100ScannerFactory(),
        AratekScannerFactory(),
        DemoScannerFactory()
    ), override val matcher: Matcher = SourceAFISMatcher()
) : Dependencies
