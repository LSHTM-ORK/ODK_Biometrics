package uk.ac.lshtm.keppel.android.settings

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import uk.ac.lshtm.keppel.android.R
import uk.ac.lshtm.keppel.core.Analytics

class UsbDeviceAttachedActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Analytics.log("usb_connected")
        Toast.makeText(this, R.string.scanner_connected, Toast.LENGTH_LONG).show()
        finish()
    }
}
