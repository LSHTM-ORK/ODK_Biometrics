package uk.ac.lshtm.keppel.cli.subject

import uk.ac.lshtm.keppel.cli.Matcher
import java.io.File

object SubjectUseCases {

    @Throws(BadHeaderException::class)
    fun parseCsv(file: String): List<Subject> {
        val inputCsv = File(file)
        val csvLines = inputCsv.readLines()
        if (!hasValidCsvHeader(csvLines)) {
            throw BadHeaderException()
        }

        return csvLines.drop(1).map {
            val split = it.split(", ")
            val templates = split.drop(1)
            Subject(split[0], templates)
        }
    }

    fun findMatches(subjects: List<Subject>, matcher: Matcher, threshold: Double): List<Match> {
        return subjects.uniqueCombinations().fold(emptyList()) { matches, combination ->
            val subject = combination.first

            val newMatches = combination.second.map { other ->
                (0.until(subject.templates.size)).map {
                    val score = matcher.match(subject.templates[it].toByteArray(), other.templates[it].toByteArray())
                    Match(subject.id, other.id, score)
                }.maxBy { it.score }
            }.filterNotNull().filter { it.score > threshold }

            matches + newMatches
        }
    }

    private fun <A> List<A>.uniqueCombinations(): Sequence<Pair<A, List<A>>> {
        // Adapted from: https://stackoverflow.com/a/59144418/166053
        return this.asSequence().mapIndexed { i, v ->
            Pair(v, this.subList(i + 1, this.size))
        }.filter { (_, subLst) ->
            subLst.isNotEmpty()
        }
    }

    private fun hasValidCsvHeader(csv: List<String>): Boolean {
        return csv.isNotEmpty() && csv[0].split(", ")[0] == "id"
    }

    data class Match(val id1: String, val id2: String, val score: Double)
    class BadHeaderException : Exception()
}