package uk.ac.lshtm.keppel.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import java.io.File

class MatchCommand(
        private val matcher: Matcher,
        private val threshold: Double,
        private val logger: Logger) : CliktCommand(name = "match") {

    private val plainText by option("-p", help = Strings.PLAIN_TEXT_HELP).flag(default = false)
    private val matchWithScore by option("-ms", help = Strings.MATCH_WITH_SCORE_HELP).flag(default = false)
    private val templateOne by argument(name = "TEMPLATE_ONE")
    private val templateTwo by argument(name = "TEMPLATE_TWO")

    override fun run() {
        val (templateOne, templateTwo) = if (plainText) {
            Pair(templateOne.toByteArray(), templateTwo.toByteArray())
        } else {
            Pair(readAndTrim(File(templateOne)), readAndTrim(File(templateTwo)))
        }

        val score = matcher.match(templateOne, templateTwo)
        if (matchWithScore) {
            if (score >= threshold) {
                logger.log("match_$score")
            } else {
                logger.log("mismatch_$score")
            }
        } else {
            logger.log("$score")
        }
    }

    private fun readAndTrim(file: File) = String(file.readBytes()).trim().toByteArray()
}