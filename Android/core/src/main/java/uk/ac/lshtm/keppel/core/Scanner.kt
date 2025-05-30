package uk.ac.lshtm.keppel.core

interface Scanner {

    /**
     * [onConnected] should receive `true` or `false` depending on if the scanner successfully
     * connected or not.
     */
    fun connect(onConnected: (Boolean) -> Unit): Scanner

    fun onDisconnect(onDisconnected: () -> Unit)

    /**
     * Waits for scanner to capture finger. Should return `null` if [stopCapture] is called while
     * [capture] is blocking.
     */
    fun capture(): CaptureResult?

    fun stopCapture()

    fun disconnect()
}

/**
 * Contains a hex encoded string of the ISO 19794-2 Template from a finger
 * placed on the scanner and the NFIQ score of the scan.
 */
data class CaptureResult(val isoTemplate: String, val nfiq: Int)
