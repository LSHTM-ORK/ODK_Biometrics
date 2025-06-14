package uk.ac.lshtm.keppel.core

fun ByteArray.toHexString() = asUByteArray().joinToString("") { it.toString(16).padStart(2, '0') }
fun String.toHexString() = this.toByteArray().toHexString()
fun String.fromHex(): ByteArray? {
    try {
        return chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    } catch (e: NumberFormatException) {
        return null
    }
}
