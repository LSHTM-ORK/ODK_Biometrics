package uk.ac.lshtm.keppel.android.matching

import com.machinezoo.sourceafis.FingerprintCompatibility.convert
import com.machinezoo.sourceafis.FingerprintMatcher
import uk.ac.lshtm.keppel.core.Matcher

class SourceAFISMatcher : Matcher {
    override fun match(one: ByteArray, two: ByteArray): Double? {
        return try {
            FingerprintMatcher()
                .index(convert(one))
                .match(convert(two))
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}
