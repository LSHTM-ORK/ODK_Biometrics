package uk.ac.lshtm.keppel.cli.support

import org.apache.commons.codec.binary.Hex
import uk.ac.lshtm.keppel.cli.Matcher

class FakeMatcher : Matcher {

    override fun match(one: ByteArray, two: ByteArray): Double {
        val oneFinger = String(Hex.decodeHex(String(one)))
        val (twoFinger, score) = String(Hex.decodeHex(String(two))).split("_")

        return if (oneFinger == twoFinger) {
            score.toDouble()
        } else {
            MISMATCH_SCORE
        }
    }

    companion object {
        const val MISMATCH_SCORE = 5.0
    }
}