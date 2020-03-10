package uk.ac.lshtm.keppel.android.scanning

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import uk.ac.lshtm.keppel.core.Scanner
import uk.ac.lshtm.keppel.core.TaskRunner

@RunWith(RobolectricTestRunner::class)
class ScannerViewModelTest {

    @Test
    fun capture_whenResultIsNull_resetsStateToConnected() {
        val scanner = mock(Scanner::class.java)
        val viewModel = ScannerViewModel(scanner, InstantTaskRunner())
        val state = viewModel.scannerState

        `when`(scanner.captureISOTemplate()).thenReturn(null)
        viewModel.capture()
        assertThat(state.value, equalTo(ScannerState.CONNECTED))
    }

    @Test
    fun onCleared_disconnectsScanner() {
        val scanner = mock(Scanner::class.java)
        val viewModel = ScannerViewModel(scanner, InstantTaskRunner())

        viewModel.onCleared()
        verify(scanner).disconnect()
    }
}

private class InstantTaskRunner : TaskRunner {

    override fun execute(runnable: () -> Unit) {
        runnable()
    }
}