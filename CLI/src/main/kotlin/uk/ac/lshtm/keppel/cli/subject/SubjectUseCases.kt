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
        return subjects.uniquePairs().parallelFold(parallelism ?: 2) { pair ->
            val scores = pair.first.templates.zip(pair.second.templates).map { (one, two) ->
                matcher.match(one.toByteArray(), two.toByteArray())
            }

            if (scores.any { it >= threshold }) {
                setOf(Match(pair.first.id, pair.second.id, scores))
            } else {
                emptySet()
            }
        }.toList()
    }

    data class Match(val id1: String, val id2: String, val scores: List<Double>)
}