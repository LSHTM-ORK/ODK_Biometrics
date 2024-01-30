package uk.ac.lshtm.keppel.core

interface Scanner {

    fun connect(onConnected: () -> Unit): Scanner

    fun onDisconnect(onDisconnected: () -> Unit)

    fun capture(): CaptureResult?

    fun stopCapture()

    fun disconnect()
}

/**
 * Contains a hex encoded string of the ISO 19794-2 Template from a finger
 * placed on the scanner and the NFIQ score of the scan.
 */
data class CaptureResult(val isoTemplate: String, val nfiq: Int)
