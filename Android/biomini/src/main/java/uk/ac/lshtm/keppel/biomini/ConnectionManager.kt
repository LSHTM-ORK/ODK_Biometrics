package uk.ac.lshtm.keppel.biomini

internal interface ConnectionManager {
    fun connect()
    fun disconnect()
    fun supportedDevices(): List<String>
}
