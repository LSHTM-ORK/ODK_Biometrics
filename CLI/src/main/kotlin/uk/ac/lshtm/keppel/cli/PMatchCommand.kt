package uk.ac.lshtm.keppel.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import uk.ac.lshtm.keppel.cli.subject.SubjectParser
import uk.ac.lshtm.keppel.cli.subject.SubjectUseCases
import java.io.File

class PMatchCommand(private val matcher: Matcher, private val defaultThreshold: Double) :
    CliktCommand(name = "pmatch") {

    private val inputCsvPath by option("-i").required()
    private val outputCsvPath by option("-o").required()

    override fun run() {
        val subjects = try {
            SubjectParser.parseCsv(inputCsvPath)
        } catch (e: SubjectParser.BadHeaderException) {
            throw IllegalArgumentException(Strings.ERROR_PMATCH_NO_HEADER_ROW)
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