package uk.ac.lshtm.keppel.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import java.io.File

class PMatch(private val matcher: Matcher, private val defaultThreshold: Double) : CliktCommand(name = "pmatch") {

    private val inputCsvPath by option("-i").required()
    private val outputCsvPath by option("-o").required()

    override fun run() {
        val inputCsv = File(inputCsvPath)
        val subjects = inputCsv.readLines().drop(1).map {
            val split = it.split(", ")
            Subject(split[0], split[1])
        }

        val matches = subjects.uniqueCombinations().fold(emptyList<Match>()) { matches, combination ->
            val subject = combination.first

            val newMatches = combination.second.map {
                val score = matcher.match(subject.template.toByteArray(), it.template.toByteArray())
                Match(subject.id, it.id, score)
            }.filter { it.score > defaultThreshold }

            matches + newMatches
        }

        val outputCsv = File(outputCsvPath)
        outputCsv.printWriter().use { writer ->
            writer.println("id_1, id_2, score_1")
            matches.forEach {
                writer.println("${it.id1}, ${it.id2}, ${it.score}")
            }
        }
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

private data class Subject(val id: String, val template: String)
private data class Match(val id1: String, val id2: String, val score: Double)