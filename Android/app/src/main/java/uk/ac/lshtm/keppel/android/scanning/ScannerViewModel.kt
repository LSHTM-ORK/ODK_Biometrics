package uk.ac.lshtm.keppel.android.scanning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.ac.lshtm.keppel.android.scanning.ScannerViewModel.ScannerState.Connected
import uk.ac.lshtm.keppel.android.scanning.ScannerViewModel.ScannerState.Disconnected
import uk.ac.lshtm.keppel.android.scanning.ScannerViewModel.ScannerState.Scanning
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

    private val _scannerState = MutableLiveData<ScannerState>(Disconnected)
    private val _result = MutableLiveData<Result?>(null)

    val scannerState: LiveData<ScannerState> = _scannerState
    val result: LiveData<Result?> = _result

    init {
        scanner.connect {
            _scannerState.value = Connected
        }

        scanner.onDisconnect {
            _scannerState.value = Disconnected
        }
    }

    fun capture(inputTemplate: String? = null) {
        _scannerState.value = Scanning

        taskRunner.execute {
            val capture = scanner.capture()
            if (inputTemplate != null && capture != null) {
                val decodedInputTemplate = inputTemplate.fromHex()
                if (decodedInputTemplate != null) {
                    val score = matcher.match(decodedInputTemplate, capture.isoTemplate.fromHex()!!)
                    _result.postValue(Result.Match(score))
                } else {
                    _result.postValue(Result.Error)
                }
            } else if (capture != null) {
                _result.postValue(Result.Scan(capture))
            } else {
                _result.postValue(null)
            }

            _scannerState.postValue(Connected)
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
        object Error : Result()
    }

    sealed class ScannerState {
        object Disconnected : ScannerState()
        object Connected : ScannerState()
        object Scanning : ScannerState()
    }
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
