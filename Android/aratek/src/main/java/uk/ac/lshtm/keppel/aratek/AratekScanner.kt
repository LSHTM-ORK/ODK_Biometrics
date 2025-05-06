package uk.ac.lshtm.keppel.aratek

import android.content.Context
import uk.ac.lshtm.keppel.core.CaptureResult
import uk.ac.lshtm.keppel.core.Scanner

class AratekScanner(context: Context) : Scanner {
    override fun connect(onConnected: (Boolean) -> Unit): Scanner {
        TODO("Not yet implemented")
    }

    override fun onDisconnect(onDisconnected: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun capture(): CaptureResult? {
        TODO("Not yet implemented")
    }

    override fun stopCapture() {
        TODO("Not yet implemented")
    }

    override fun disconnect() {
        TODO("Not yet implemented")
    }
}
