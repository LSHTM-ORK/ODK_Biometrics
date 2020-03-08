package uk.ac.lshtm.mantra.android.scanning

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_scan.*
import uk.ac.lshtm.mantra.android.R
import uk.ac.lshtm.mantra.core.Scanner
import uk.ac.lshtm.mantra.mantramfs100.MFS100Scanner

class ScanActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        val scanner = SCANNER_FACTORY.create(this)
        scanner.connect {
            connect_progress_bar.visibility = View.GONE
            capture_button.visibility = View.VISIBLE
            capture_progress_bar.visibility = View.GONE
        }

        capture_button.setOnClickListener {
            connect_progress_bar.visibility = View.GONE
            capture_button.visibility = View.GONE
            capture_progress_bar.visibility = View.VISIBLE

            Thread(Runnable {
                val isoTemplate = scanner.captureISOTemplate()
                intent.putExtra("value", isoTemplate)
                setResult(RESULT_OK, intent)
                finish()
            }).start()
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