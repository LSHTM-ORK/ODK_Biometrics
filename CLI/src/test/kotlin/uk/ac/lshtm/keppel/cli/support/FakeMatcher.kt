package uk.ac.lshtm.keppel.cli.support

import org.apache.commons.codec.binary.Hex
import uk.ac.lshtm.keppel.cli.Matcher

class FakeMatcher : Matcher {

    private val scores = mutableListOf<Triple<String, String, Double>>()

    override fun match(one: ByteArray, two: ByteArray): Double {
        val stringOne = String(Hex.decodeHex(String(one)))
        val stringTwo = String(Hex.decodeHex(String(two)))

        val score = scores.find {
            (it.first == stringOne && it.second == stringTwo) || (it.first == stringTwo && it.second == stringOne)
        }

        if (score != null) {
            return score.third
        } else {
            throw IllegalStateException("No score for $stringOne and $stringTwo!")
        }
    }

    fun addScore(one: String, two: String, score: Double) {
        scores.add(Triple(one, two, score))
    }
}