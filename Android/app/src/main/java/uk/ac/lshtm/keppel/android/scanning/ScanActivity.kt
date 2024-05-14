package uk.ac.lshtm.keppel.android.scanning

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import uk.ac.lshtm.keppel.android.External
import uk.ac.lshtm.keppel.android.OdkExternal
import uk.ac.lshtm.keppel.android.OdkExternalRequest
import uk.ac.lshtm.keppel.android.R
import uk.ac.lshtm.keppel.android.databinding.ActivityScanBinding
import uk.ac.lshtm.keppel.android.matcher
import uk.ac.lshtm.keppel.android.scannerFactory
import uk.ac.lshtm.keppel.android.scanning.ScannerViewModel.ScannerState
import uk.ac.lshtm.keppel.android.taskRunner
import uk.ac.lshtm.keppel.core.Analytics
import uk.ac.lshtm.keppel.core.CaptureResult

class ScanActivity : AppCompatActivity() {

    private val viewModelFactory = viewModelFactory {
        addInitializer(ScannerViewModel::class) {
            ScannerViewModel(scannerFactory().create(this@ScanActivity), matcher(), taskRunner())
        }
    }

    private val viewModel: ScannerViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val request: Request = IntentParser.parse(intent)

        if (request is Request.Match) {
            if (request.isoTemplate == null) {
                val error = getString(R.string.input_missing_error, External.PARAM_ISO_TEMPLATE)

                MaterialAlertDialogBuilder(this)
                    .setMessage(error)
                    .setPositiveButton(R.string.ok) { _, _ -> finish() }
                    .show()

                return
            }
        }

        viewModel.scannerState.observe(this) { state ->
            when (state) {
                ScannerState.Disconnected -> {
                    binding.connectProgressBar.visibility = View.VISIBLE
                    binding.captureButton.visibility = View.GONE
                    binding.captureProgressBar.visibility = View.GONE
                }

                ScannerState.Connected -> {
                    if (request.fast) {
                        capture(request)
                    } else {
                        binding.connectProgressBar.visibility = View.GONE
                        binding.captureButton.visibility = View.VISIBLE
                        binding.captureProgressBar.visibility = View.GONE
                    }
                }

                ScannerState.Scanning -> {
                    binding.connectProgressBar.visibility = View.GONE
                    binding.captureButton.visibility = View.GONE
                    binding.captureProgressBar.visibility = View.VISIBLE
                }
            }
        }

        viewModel.result.observe(this) { result ->
            if (result != null) {
                processResult(request, result)
            }
        }

        binding.captureButton.setOnClickListener {
            capture(request)
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }

        if (request is Request.Match) {
            binding.captureButton.setText(R.string.match)
        }
    }

    private fun capture(action: Request) {
        if (action is Request.Match) {
            viewModel.capture(action.isoTemplate)
        } else {
            viewModel.capture()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopCapture()
    }

    private fun processResult(request: Request, result: ScannerViewModel.Result) {
        when (result) {
            is ScannerViewModel.Result.Match -> {
                returnResult(
                    buildMatchReturn(
                        request.odkExternalRequest,
                        result.score,
                        result.captureResult
                    )
                )
            }

            is ScannerViewModel.Result.Scan -> {
                returnResult(buildScanReturn(request.odkExternalRequest, result.captureResult))
            }

            else -> {
                MaterialAlertDialogBuilder(this)
                    .setMessage(R.string.input_format_error)
                    .setPositiveButton(R.string.ok) { _, _ -> finish() }
                    .show()
            }
        }
    }

    private fun returnResult(result: Intent) {
        setResult(RESULT_OK, result)
        finish()
        Analytics.log("return_result")
    }

    private fun buildScanReturn(
        odkExternalRequest: OdkExternalRequest,
        capture: CaptureResult
    ): Intent {
        return if (OdkExternal.isSingleReturn(odkExternalRequest)) {
            OdkExternal.buildSingleReturnIntent(capture.isoTemplate)
        } else {
            OdkExternal.buildMultipleReturnResult(
                odkExternalRequest.params, mapOf(
                    External.PARAM_RETURN_ISO_TEMPLATE to capture.isoTemplate,
                    External.PARAM_RETURN_NFIQ to capture.nfiq
                )
            )
        }
    }

    private fun buildMatchReturn(
        odkExternalRequest: OdkExternalRequest,
        score: Double,
        capture: CaptureResult
    ): Intent {
        return if (OdkExternal.isSingleReturn(odkExternalRequest)) {
            OdkExternal.buildSingleReturnIntent(score)
        } else {
            OdkExternal.buildMultipleReturnResult(
                odkExternalRequest.params, mapOf(
                    External.PARAM_RETURN_SCORE to score,
                    External.PARAM_RETURN_ISO_TEMPLATE to capture.isoTemplate,
                    External.PARAM_RETURN_NFIQ to capture.nfiq
                )
            )
        }
    }
}
