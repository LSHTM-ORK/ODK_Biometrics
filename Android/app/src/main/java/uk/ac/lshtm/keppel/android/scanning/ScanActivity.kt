package uk.ac.lshtm.keppel.android.scanning

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import uk.ac.lshtm.keppel.android.OdkExternal
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

        viewModel.scannerState.observe(this) { state ->
            when (state) {
                ScannerState.Disconnected -> {
                    binding.connectProgressBar.visibility = View.VISIBLE
                    binding.captureButton.visibility = View.GONE
                    binding.captureProgressBar.visibility = View.GONE
                }

                ScannerState.Connected -> {
                    binding.connectProgressBar.visibility = View.GONE
                    binding.captureButton.visibility = View.VISIBLE
                    binding.captureProgressBar.visibility = View.GONE
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
                processResult(result)
            }
        }

        binding.captureButton.setOnClickListener {
            if (intent.action == OdkExternal.ACTION_MATCH) {
                val inputTemplate = intent.extras!!.getString(OdkExternal.PARAM_ISO_TEMPLATE)
                viewModel.capture(inputTemplate)
            } else {
                viewModel.capture()
            }
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }

        if (intent.action == OdkExternal.ACTION_MATCH) {
            binding.captureButton.setText(R.string.match)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopCapture()
    }

    private fun processResult(result: ScannerViewModel.Result) {
        when (result) {
            is ScannerViewModel.Result.Match -> {
                returnResult(buildMatchReturn(result.score))
            }

            is ScannerViewModel.Result.Scan -> {
                returnResult(buildScanReturn(this.intent, result.captureResult))
            }

            else -> {
                MaterialAlertDialogBuilder(this)
                    .setMessage(R.string.input_hex_error)
                    .setPositiveButton(R.string.ok) { _, _ -> finish() }
                    .show()
            }
        }
    }

    private fun returnResult(intent: Intent) {
        setResult(RESULT_OK, intent)
        finish()
        Analytics.log("return_result")
    }

    private fun buildScanReturn(inputIntent: Intent, capture: CaptureResult): Intent {
        val intent = Intent()

        if (inputIntent.extras?.containsKey(OdkExternal.PARAM_INPUT_VALUE) == false) {
            if (inputIntent.hasExtra(OdkExternal.PARAM_RETURN_ISO_TEMPLATE)) {
                intent.putExtra(
                    inputIntent.getStringExtra(OdkExternal.PARAM_RETURN_ISO_TEMPLATE),
                    capture.isoTemplate
                )
            }

            if (inputIntent.hasExtra(OdkExternal.PARAM_RETURN_NFIQ)) {
                intent.putExtra(
                    inputIntent.getStringExtra(OdkExternal.PARAM_RETURN_NFIQ),
                    capture.nfiq
                )
            }
        } else {
            intent.putExtra(OdkExternal.PARAM_RETURN_VALUE, capture.isoTemplate)
        }

        return intent
    }

    private fun buildMatchReturn(score: Double): Intent {
        intent.putExtra(OdkExternal.PARAM_RETURN_VALUE, score)
        return intent
    }
}
