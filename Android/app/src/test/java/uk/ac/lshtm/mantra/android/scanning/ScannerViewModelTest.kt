package uk.ac.lshtm.mantra.android.scanning

import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import uk.ac.lshtm.mantra.core.Scanner

class ScannerViewModelTest {

    @Test
    fun onCleared_disconnectsScanner() {
        val scanner = mock(Scanner::class.java)
        val viewModel = ScannerViewModel(scanner)

        viewModel.onCleared()
        verify(scanner).disconnect()
    }
}