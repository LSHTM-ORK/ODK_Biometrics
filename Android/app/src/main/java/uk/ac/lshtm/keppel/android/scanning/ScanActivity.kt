package uk.ac.lshtm.keppel.android.scanning

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import uk.ac.lshtm.keppel.android.Actions
import uk.ac.lshtm.keppel.android.databinding.ActivityScanBinding
import uk.ac.lshtm.keppel.android.scannerFactory
import uk.ac.lshtm.keppel.android.taskRunner
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

        viewModel.fingerData.observe(this) { capture ->
            if (capture != null) {
                returnCapture(capture)
            }
        }

        binding.captureButton.setOnClickListener {
            viewModel.capture()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopCapture()
    }

    private fun returnCapture(capture: CaptureResult) {
        val returnIntent = Intent().also {
            if (intent.extras?.containsKey(Actions.EXTRA_ODK_INPUT_VALUE) == false) {
                if (intent.hasExtra(Actions.Scan.EXTRA_ISO_TEMPLATE)) {
                    it.putExtra(Actions.Scan.EXTRA_ISO_TEMPLATE, capture.isoTemplate)
                }

                if (intent.hasExtra(Actions.Scan.EXTRA_NFIQ)) {
                    it.putExtra(Actions.Scan.EXTRA_NFIQ, capture.nfiq)
                }
            } else {
                it.putExtra(Actions.Scan.EXTRA_ODK_RETURN_VALUE, capture.isoTemplate)
            }
        }

        setResult(RESULT_OK, returnIntent)
        finish()
    }
}
