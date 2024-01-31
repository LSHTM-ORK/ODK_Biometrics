package uk.ac.lshtm.keppel.android.scanning

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import uk.ac.lshtm.keppel.android.scanning.ScannerViewModel.ScannerState
import uk.ac.lshtm.keppel.core.Scanner
import uk.ac.lshtm.keppel.core.TaskRunner

@RunWith(AndroidJUnit4::class)
class ScannerViewModelTest {

    @get:Rule
    val instanceTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun capture_whenResultIsNull_resetsStateToConnected() {
        val scanner = mock(Scanner::class.java)
        val viewModel = ScannerViewModel(scanner, mock(), InstantTaskRunner())
        val state = viewModel.scannerState

        `when`(scanner.capture()).thenReturn(null)
        viewModel.capture()
        assertThat(state.value, equalTo(ScannerState.Connected))
    }

    @Test
    fun onCleared_disconnectsScanner() {
        val scanner = mock(Scanner::class.java)
        val viewModel = ScannerViewModel(scanner, mock(), InstantTaskRunner())

        viewModel.onCleared()
        verify(scanner).disconnect()
    }

    @Test
    fun onCleared_stopsScannerCapture() {
        val scanner = mock(Scanner::class.java)
        val viewModel = ScannerViewModel(scanner, mock(), InstantTaskRunner())

        viewModel.onCleared()
        verify(scanner).stopCapture()
    }
}

private class InstantTaskRunner : TaskRunner {

    override fun execute(runnable: () -> Unit) {
        runnable()
    }
}
