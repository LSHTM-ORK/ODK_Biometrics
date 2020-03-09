package uk.ac.lshtm.mantra.cli

import com.machinezoo.sourceafis.FingerprintCompatibility.convert
import com.machinezoo.sourceafis.FingerprintMatcher
import org.apache.commons.codec.binary.Hex

class SourceAFISMatcher : Matcher {

    override fun match(one: ByteArray, two: ByteArray): Double {
        val oneTemplate = convert(Hex.decodeHex(String(one)))
        val twoTemplate = convert(Hex.decodeHex(String(two)))

        return FingerprintMatcher()
                .index(oneTemplate)
                .match(twoTemplate)
    }
}