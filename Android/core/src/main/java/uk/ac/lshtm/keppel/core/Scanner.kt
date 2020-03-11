package uk.ac.lshtm.keppel.core

interface Scanner {

    fun connect(onConnected: () -> Unit): Scanner

    fun onDisconnect(onDisconnected: () -> Unit)

    /**
     * Captures and returns a hex encoded string of the ISO 19794-2 Template from a finger
     * placed on the scanner
     */
    fun captureISOTemplate(): String?

    fun stopCapture()

    fun disconnect()
}