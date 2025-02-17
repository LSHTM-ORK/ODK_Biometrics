package uk.ac.lshtm.keppel.android.scanning

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import uk.ac.lshtm.keppel.android.scanning.ScannerViewModel.ScannerState
import uk.ac.lshtm.keppel.core.CaptureResult
import uk.ac.lshtm.keppel.core.Scanner
import uk.ac.lshtm.keppel.core.TaskRunner

@RunWith(AndroidJUnit4::class)
class ScannerViewModelTest {

    @get:Rule
    val instanceTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun capture_whenResultIsNull_resetsStateToConnected() {
        val scanner = mock<Scanner>()
        val viewModel = ScannerViewModel(scanner, mock(), InstantTaskRunner())
        val state = viewModel.scannerState

        `when`(scanner.capture()).thenReturn(null)
        viewModel.capture()
        assertThat(state.value, equalTo(ScannerState.Connected))
    }

    @Test
    fun onCleared_disconnectsScanner() {
        val scanner = mock<Scanner>()
        val viewModel = ScannerViewModel(scanner, mock(), InstantTaskRunner())

        viewModel.onCleared()
        verify(scanner).disconnect()
    }

    @Test
    fun onCleared_stopsScannerCapture() {
        val scanner = mock<Scanner>()
        val viewModel = ScannerViewModel(scanner, mock(), InstantTaskRunner())

        viewModel.onCleared()
        verify(scanner).stopCapture()
    }

    @Test
    fun whenFastMode_whenScannerConnected_startsCapturing() {
        val scanner = MockScanner()
        val viewModel = ScannerViewModel(scanner, mock(), InstantTaskRunner(), fast = true)
        assertThat(scanner.captures, equalTo(0))

        scanner.connect()
        assertThat(scanner.captures, equalTo(1))
    }
}

private class InstantTaskRunner : TaskRunner {

    override fun execute(runnable: () -> Unit) {
        runnable()
    }
}

private class MockScanner : Scanner {

    var captures: Int = 0
        private set

    private var onConnected: ((Boolean) -> Unit)? = null
    private var onDisconnected: (() -> Unit)? = null

    override fun connect(onConnected: (Boolean) -> Unit): Scanner {
        this.onConnected = onConnected
        return this
    }

    override fun onDisconnect(onDisconnected: () -> Unit) {
        this.onDisconnected = onDisconnected
    }

    override fun capture(): CaptureResult? {
        captures += 1
        return null
    }

    override fun stopCapture() {

    }

    override fun disconnect() {
        onDisconnected?.invoke()
    }

    fun connect() {
        onConnected?.invoke(true)
    }
}
