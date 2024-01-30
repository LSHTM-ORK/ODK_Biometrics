package uk.ac.lshtm.keppel.android.scanning

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import uk.ac.lshtm.keppel.android.databinding.ActivityScanBinding
import uk.ac.lshtm.keppel.android.scannerFactory
import uk.ac.lshtm.keppel.android.taskRunner

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
        ).get(ScannerViewModel::class.java)

        viewModel.scannerState.observe(this, Observer { state ->
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
        })

        viewModel.fingerTemplate.observe(this, Observer { template ->
            if (template != null) {
                intent.putExtra("value", template)
                setResult(RESULT_OK, intent)
                finish()
            }
        })

        binding.captureButton.setOnClickListener {
            viewModel.capture()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopCapture()
    }
}
