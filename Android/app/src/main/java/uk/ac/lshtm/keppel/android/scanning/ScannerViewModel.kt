package uk.ac.lshtm.keppel.android.scanning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.ac.lshtm.keppel.android.scanning.ScannerState.CONNECTED
import uk.ac.lshtm.keppel.android.scanning.ScannerState.DISCONNECTED
import uk.ac.lshtm.keppel.android.scanning.ScannerState.SCANNING
import uk.ac.lshtm.keppel.core.CaptureResult
import uk.ac.lshtm.keppel.core.Matcher
import uk.ac.lshtm.keppel.core.Scanner
import uk.ac.lshtm.keppel.core.TaskRunner
import uk.ac.lshtm.keppel.core.fromHex

class ScannerViewModel(
    private val scanner: Scanner,
    private val matcher: Matcher,
    private val taskRunner: TaskRunner
) : ViewModel() {

    private val _scannerState = MutableLiveData(DISCONNECTED)
    private val _fingerData = MutableLiveData<Result?>(null)

    val scannerState: LiveData<ScannerState> = _scannerState
    val fingerData: LiveData<Result?> = _fingerData

    init {
        scanner.connect {
            _scannerState.value = CONNECTED
        }

        scanner.onDisconnect {
            _scannerState.value = DISCONNECTED
        }
    }

    fun capture(inputTemplate: String? = null) {
        _scannerState.value = SCANNING

        taskRunner.execute {
            val capture = scanner.capture()
            if (inputTemplate != null && capture != null) {
                val score = matcher.match(inputTemplate.fromHex(), capture.isoTemplate.fromHex())
                _fingerData.postValue(Result.Match(score))
            } else if (capture != null) {
                _fingerData.postValue(Result.Scan(capture))
            } else {
                _fingerData.postValue(null)
            }

            _scannerState.postValue(CONNECTED)
        }
    }

    fun stopCapture() {
        scanner.stopCapture()
    }

    public override fun onCleared() {
        scanner.stopCapture()
        scanner.disconnect()
    }

    sealed class Result {
        data class Scan(val captureResult: CaptureResult) : Result()
        data class Match(val score: Double) : Result()
    }
}

enum class ScannerState {
    DISCONNECTED, CONNECTED, SCANNING
}

class ScannerViewModelFactory(
    private val scanner: Scanner,
    private val matcher: Matcher,
    private val taskRunner: TaskRunner
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScannerViewModel(scanner, matcher, taskRunner) as T
    }
}
