package uk.ac.lshtm.keppel.biomini.eacrugged

import uk.ac.lshtm.keppel.biomini.ConnectionManager

class EACRuggedConnectionManager : ConnectionManager {
    override fun connect() {
        IoControl.getInstance().setIoStatus(
            IoControl.USBFP_PATH,
            IoControl.IOSTATUS.ENABLE,
        )
    }

    override fun disconnect() {
        IoControl.getInstance().setIoStatus(
            IoControl.USBFP_PATH,
            IoControl.IOSTATUS.DISABLE,
        )
    }

    override fun supportedDevices(): List<String> {
        return listOf("EACRUGGED RG80")
    }
}
