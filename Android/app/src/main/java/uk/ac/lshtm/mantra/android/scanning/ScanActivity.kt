package uk.ac.lshtm.mantra.android.scanning

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_scan.*
import uk.ac.lshtm.mantra.android.R
import uk.ac.lshtm.mantra.core.Scanner
import uk.ac.lshtm.mantra.mantramfs100.MFS100Scanner

class ScanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        val scanner = SCANNER_FACTORY.create(this)
        val viewModel = ViewModelProvider(this, ScannerViewModelFactory(scanner))
            .get(ScannerViewModel::class.java)

        viewModel.scannerState.observe(this, Observer { state ->
            when (state) {
                ScannerState.DISCONNECTED -> {
                    connect_progress_bar.visibility = View.VISIBLE
                    capture_button.visibility = View.GONE
                    capture_progress_bar.visibility = View.GONE
                }

                ScannerState.CONNECTED -> {
                    connect_progress_bar.visibility = View.GONE
                    capture_button.visibility = View.VISIBLE
                    capture_progress_bar.visibility = View.GONE
                }

                ScannerState.SCANNING -> {
                    connect_progress_bar.visibility = View.GONE
                    capture_button.visibility = View.GONE
                    capture_progress_bar.visibility = View.VISIBLE
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

        capture_button.setOnClickListener {
            viewModel.capture()
        }
    }

    companion object {
        var SCANNER_FACTORY: ScannerFactory = object : ScannerFactory {
            override fun create(context: Context): Scanner {
                return MFS100Scanner(context)
            }
        }
    }
}