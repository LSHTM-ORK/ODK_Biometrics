package uk.ac.lshtm.keppel.cli

import java.io.File

class MatchCommand(
        private val matcher: Matcher,
        private val threshold: Double) {

    fun execute(args: Array<String>, logger: Logger): Boolean {
        val score = matcher.match(readAndTrim(File(args[0])), readAndTrim(File(args[1])))
        if (score >= threshold) {
            logger.log("Match! Score: $score")
            return true
        } else {
            logger.log("Not a match. Score: $score")
            return false
        }
    }

    private fun readAndTrim(file: File) = String(file.readBytes()).trim().toByteArray()
}