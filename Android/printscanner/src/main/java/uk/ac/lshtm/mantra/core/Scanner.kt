package uk.ac.lshtm.mantra.core

interface Scanner {

    fun connect(onConnected: () -> Unit)

    /**
     * Captures and returns a hex encoded string of the ISO 19794-2 Template from a finger
     * placed on the scanner
     */
    fun captureISOTemplate(): String

}