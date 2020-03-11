package uk.ac.lshtm.keppel.android.scanning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.ac.lshtm.keppel.android.scanning.ScannerState.*
import uk.ac.lshtm.keppel.core.Scanner
import uk.ac.lshtm.keppel.core.TaskRunner

class ScannerViewModel(
    private val scanner: Scanner,
    private val taskRunner: TaskRunner
) : ViewModel() {

    private val _scannerState = MutableLiveData(DISCONNECTED)
    private val _fingerTemplate = MutableLiveData<String>(null)

    val scannerState: LiveData<ScannerState> = _scannerState
    val fingerTemplate: LiveData<String> = _fingerTemplate

    init {
        scanner.connect {
            _scannerState.value = CONNECTED
        }

        scanner.onDisconnect {
            _scannerState.value = DISCONNECTED
        }
    }

    fun capture() {
        _scannerState.value = SCANNING

        taskRunner.execute {
            val isoTemplate = scanner.captureISOTemplate()
            _scannerState.postValue(CONNECTED)
            _fingerTemplate.postValue(isoTemplate)
        }
    }

    fun stopCapture() {
        scanner.stopCapture()
    }

    public override fun onCleared() {
        scanner.stopCapture()
        scanner.disconnect()
    }
}

enum class ScannerState {
    DISCONNECTED, CONNECTED, SCANNING
}

class ScannerViewModelFactory(private val scanner: Scanner, private val taskRunner: TaskRunner) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScannerViewModel(scanner, taskRunner) as T
    }
}