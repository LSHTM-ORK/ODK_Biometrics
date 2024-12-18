package uk.ac.lshtm.keppel.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import uk.ac.lshtm.keppel.cli.subject.Subject
import uk.ac.lshtm.keppel.cli.subject.SubjectUseCases
import java.io.File

class PMatch(private val matcher: Matcher, private val defaultThreshold: Double) : CliktCommand(name = "pmatch") {

    private val inputCsvPath by option("-i").required()
    private val outputCsvPath by option("-o").required()

    override fun run() {
        val inputCsv = File(inputCsvPath)
        val subjects = inputCsv.readLines().drop(1).map {
            val split = it.split(", ")
            val templates = split.drop(1)
            Subject(split[0], templates)
        }

        val matches = SubjectUseCases.findMatches(subjects, matcher, defaultThreshold)

        val outputCsv = File(outputCsvPath)
        outputCsv.printWriter().use { writer ->
            writer.println("id_1, id_2, score_1")
            matches.forEach {
                writer.println("${it.id1}, ${it.id2}, ${it.score}")
            }
        }
    }
}