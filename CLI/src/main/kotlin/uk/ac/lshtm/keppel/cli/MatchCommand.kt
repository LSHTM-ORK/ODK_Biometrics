package uk.ac.lshtm.keppel.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.double
import java.io.File
import java.nio.file.Paths

class MatchCommand(
        private val matcher: Matcher,
        private val defaultThreshold: Double,
        private val logger: Logger) : CliktCommand(
        name = "match",
        help = "Match two hex encoded ISO fingerprint templates. Threshold used for matching is $defaultThreshold.") {

    private val plainText by option("-p", help = Strings.PLAIN_TEXT_HELP).flag(default = false)
    private val image by option("-i", help = Strings.PLAIN_TEXT_HELP).flag(default = false)
    private val matchWithScore by option("-ms", help = Strings.MATCH_WITH_SCORE_HELP).flag(default = false)
    private val matchWithoutScore by option("-m", help = Strings.MATCH_WITHOUT_SCORE_HELP).flag(default = false)
    private val threshold by option("-t", help = Strings.THRESHOLD_HELP).double()

    private val templateOne by argument(name = "TEMPLATE_ONE")
    private val templateTwo by argument(name = "TEMPLATE_TWO")

    override fun run() {
        val score = if (image) {
            matcher.matchImage(Paths.get(templateOne), Paths.get(templateTwo))
        } else {
            val (templateOne, templateTwo) = if (plainText) {
                Pair(templateOne.toByteArray(), templateTwo.toByteArray())
            } else {
                Pair(readAndTrim(File(templateOne)), readAndTrim(File(templateTwo)))
            }
            matcher.match(templateOne, templateTwo)
        }

        if (matchWithScore) {
            if (isMatch(score)) {
                logger.log("match_$score")
            } else {
                logger.log("mismatch_$score")
            }
        } else if (matchWithoutScore) {
            if (isMatch(score)) {
                logger.log("match")
            } else {
                logger.log("mismatch")
            }
        } else {
            logger.log("$score")
        }
    }

    private fun isMatch(score: Double): Boolean {
        return score >= (threshold ?: defaultThreshold)
    }

    private fun readAndTrim(file: File) = String(file.readBytes()).trim().toByteArray()
}