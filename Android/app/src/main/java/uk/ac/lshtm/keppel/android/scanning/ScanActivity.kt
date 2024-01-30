package uk.ac.lshtm.keppel.android.scanning

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import uk.ac.lshtm.keppel.android.OdkExternal
import uk.ac.lshtm.keppel.android.R
import uk.ac.lshtm.keppel.android.databinding.ActivityScanBinding
import uk.ac.lshtm.keppel.android.matcher
import uk.ac.lshtm.keppel.android.scannerFactory
import uk.ac.lshtm.keppel.android.taskRunner
import uk.ac.lshtm.keppel.core.Analytics
import uk.ac.lshtm.keppel.core.CaptureResult

class ScanActivity : AppCompatActivity() {

    private lateinit var viewModel: ScannerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, ScannerViewModelFactory(
                scannerFactory().create(this),
                matcher(),
                taskRunner()
            )
        )[ScannerViewModel::class.java]

        viewModel.scannerState.observe(this) { state ->
            when (state) {
                ScannerState.DISCONNECTED -> {
                    binding.connectProgressBar.visibility = View.VISIBLE
                    binding.captureButton.visibility = View.GONE
                    binding.captureProgressBar.visibility = View.GONE
                }

                ScannerState.CONNECTED -> {
                    binding.connectProgressBar.visibility = View.GONE
                    binding.captureButton.visibility = View.VISIBLE
                    binding.captureProgressBar.visibility = View.GONE
                }

                ScannerState.SCANNING -> {
                    binding.connectProgressBar.visibility = View.GONE
                    binding.captureButton.visibility = View.GONE
                    binding.captureProgressBar.visibility = View.VISIBLE
                }

                else -> {
                    // Ignore null case - not expected
                }
            }
        }

        viewModel.fingerData.observe(this) { result ->
            if (result != null) {
                returnResult(result)
            }
        }

        binding.captureButton.setOnClickListener {
            if (intent.action == OdkExternal.ACTION_MATCH) {
                viewModel.capture(intent.extras!!.getString(OdkExternal.PARAM_ISO_TEMPLATE))
            } else {
                viewModel.capture()
            }
        }

        if (intent.action == OdkExternal.ACTION_MATCH) {
            binding.captureButton.setText(R.string.match)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopCapture()
    }

    private fun returnResult(result: ScannerViewModel.Result) {
        val returnIntent = when (result) {
            is ScannerViewModel.Result.Match -> buildMatchReturn(result.score)
            is ScannerViewModel.Result.Scan -> buildScanReturn(intent, result.captureResult)
        }

        setResult(RESULT_OK, returnIntent)
        finish()

        Analytics.log("return_result")
    }

    private fun buildScanReturn(inputIntent: Intent, capture: CaptureResult): Intent {
        val intent = Intent()

        if (inputIntent.extras?.containsKey(OdkExternal.PARAM_INPUT_VALUE) == false) {
            if (inputIntent.hasExtra(OdkExternal.PARAM_RETURN_ISO_TEMPLATE)) {
                intent.putExtra(inputIntent.getStringExtra(OdkExternal.PARAM_RETURN_ISO_TEMPLATE), capture.isoTemplate)
            }

            if (inputIntent.hasExtra(OdkExternal.PARAM_RETURN_NFIQ)) {
                intent.putExtra(inputIntent.getStringExtra(OdkExternal.PARAM_RETURN_NFIQ), capture.nfiq)
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
