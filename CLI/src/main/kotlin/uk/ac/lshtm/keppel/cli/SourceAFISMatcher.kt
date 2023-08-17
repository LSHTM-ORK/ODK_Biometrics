package uk.ac.lshtm.keppel.cli

import com.machinezoo.sourceafis.FingerprintCompatibility.importTemplate
import com.machinezoo.sourceafis.FingerprintImage
import com.machinezoo.sourceafis.FingerprintMatcher
import com.machinezoo.sourceafis.FingerprintTemplate
import org.apache.commons.codec.binary.Hex
import java.nio.file.Path
import java.nio.file.Files

class SourceAFISMatcher : Matcher {

    override fun matchImage(one: Path, two: Path): Double {
        val oneTemplate = FingerprintTemplate(FingerprintImage(Files.readAllBytes(one)))
        val twoTemplate = FingerprintTemplate(FingerprintImage(Files.readAllBytes(two)))

        return match(oneTemplate, twoTemplate)
    }

    override fun match(one: ByteArray, two: ByteArray): Double {
        val oneTemplate = importTemplate(Hex.decodeHex(String(one)))
        val twoTemplate = importTemplate(Hex.decodeHex(String(two)))

        return match(oneTemplate, twoTemplate)
    }

    override fun match(one: FingerprintTemplate, two: FingerprintTemplate): Double {
        return FingerprintMatcher(one)
                .match(two)
    }
}