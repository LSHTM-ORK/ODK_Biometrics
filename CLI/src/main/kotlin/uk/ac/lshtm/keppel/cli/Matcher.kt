package uk.ac.lshtm.keppel.cli

import com.machinezoo.sourceafis.FingerprintTemplate
import java.nio.file.Path

interface Matcher {
    fun matchImage(one: Path, two: Path): Double 
    fun match(one: ByteArray, two: ByteArray): Double
    fun match(one: FingerprintTemplate, two: FingerprintTemplate): Double
}