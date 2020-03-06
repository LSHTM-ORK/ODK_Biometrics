package uk.ac.lshtm.mantra.android.scanning

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_scan.*
import uk.ac.lshtm.mantra.android.R
import uk.ac.lshtm.mantra.core.Scanner

class ScanActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        capture_button.setOnClickListener {
            capture_button.visibility = View.GONE
            progress_bar.visibility = View.VISIBLE

            object : AsyncTask<Void, Void, String>() {
                override fun doInBackground(vararg params: Void?): String {
                    return SCANNER_FACTORY.create().captureISOTemplate()
                }

                override fun onPostExecute(template: String) {
                    val intent = Intent().apply {
                        putExtra("value", template)
                    }

                    setResult(RESULT_OK, intent)
                    finish()
                }
            }.execute()
        }
    }

    companion object {
        var SCANNER_FACTORY: ScannerFactory = object : ScannerFactory {
            override fun create(): Scanner {
                return DemoScanner()
            }
        }
    }
}
