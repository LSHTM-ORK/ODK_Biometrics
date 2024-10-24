package uk.ac.lshtm.keppel.android.matching

import com.machinezoo.sourceafis.FingerprintCompatibility.importTemplate
import com.machinezoo.fingerprintio.TemplateFormatException
import com.machinezoo.sourceafis.FingerprintMatcher
import uk.ac.lshtm.keppel.core.Matcher

class SourceAFISMatcher : Matcher {
    override fun match(one: ByteArray, two: ByteArray): Double? {
        return try {
            FingerprintMatcher(importTemplate(one))
                .match(importTemplate(two))
        } catch (e: TemplateFormatException) {
            null
        }
    }
}
