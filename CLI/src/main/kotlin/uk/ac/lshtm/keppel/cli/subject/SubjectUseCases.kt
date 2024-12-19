package uk.ac.lshtm.keppel.cli.subject

import uk.ac.lshtm.keppel.cli.Matcher
import uk.ac.lshtm.keppel.cli.util.parallelFold
import uk.ac.lshtm.keppel.cli.util.uniquePairs

object SubjectUseCases {

    fun findMatches(
        subjects: List<Subject>,
        matcher: Matcher,
        threshold: Double,
        parallelism: Int? = null
    ): List<Match> {
        return subjects.uniquePairs().toList().parallelFold(parallelism ?: 2) { pair ->
            val match = pair.first.templates.zip(pair.second.templates).map { (one, two) ->
                val score = matcher.match(one.toByteArray(), two.toByteArray())
                Match(pair.first.id, pair.second.id, score)
            }.filter {
                it.score > threshold
            }.maxByOrNull {
                it.score
            }

            if (match != null) {
                setOf(match)
            } else {
                emptySet()
            }
        }.toList()
    }

    data class Match(val id1: String, val id2: String, val score: Double)
}