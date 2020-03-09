package uk.ac.lshtm.mantra.android.scanning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.ac.lshtm.mantra.android.scanning.ScannerState.*
import uk.ac.lshtm.mantra.core.Scanner

class ScannerViewModel(private val scanner: Scanner) : ViewModel() {

    private val _scannerState = MutableLiveData(DISCONNECTED)
    private val _fingerTemplate = MutableLiveData<String>(null)

    val scannerState: LiveData<ScannerState> = _scannerState
    val fingerTemplate: LiveData<String> = _fingerTemplate

    init {
        scanner.connect {
            _scannerState.value = CONNECTED
        }
    }

    fun capture() {
        _scannerState.value = SCANNING

        Thread(Runnable {
            val isoTemplate = scanner.captureISOTemplate()
            _fingerTemplate.postValue(isoTemplate)
            _scannerState.postValue(CONNECTED)
        }).start()
    }

    public override fun onCleared() {
        scanner.disconnect()
    }
}

enum class ScannerState {
    DISCONNECTED, CONNECTED, SCANNING
}

class ScannerViewModelFactory(private val scanner: Scanner) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScannerViewModel(scanner) as T
    }
}