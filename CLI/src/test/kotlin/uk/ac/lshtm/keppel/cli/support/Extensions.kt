package uk.ac.lshtm.keppel.cli.support

import org.apache.commons.codec.binary.Hex

fun String.toHexString(): String {
    return String(Hex.encodeHex(this.toByteArray()))
}