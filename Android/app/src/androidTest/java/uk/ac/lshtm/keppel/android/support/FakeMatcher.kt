package uk.ac.lshtm.keppel.android.support

import uk.ac.lshtm.keppel.core.Matcher

class FakeMatcher : Matcher {

    private val matches = mutableMapOf<Pair<String, String>, Double?>()

    override fun match(one: ByteArray, two: ByteArray): Double? {
        val key = Pair(String(one), String(two))
        return if (matches.containsKey(key)) {
            matches[key]
        } else {
            0.0
        }
    }

    fun addScore(one: String, two: String, score: Double?) {
        matches[Pair(one, two)] = score
    }
}
