package uk.ac.lshtm.keppel.cli

import java.io.File

class MatchCommand(
        private val matcher: Matcher,
        private val threshold: Double) {

    fun execute(args: List<String>, logger: Logger): Boolean {
        if (args.size < 2) {
            logger.log("Usage: match [TEMPLATE_FILE_1] [TEMPLATE_FILE_2]")
            return false
        }

        val (templateOne, templateTwo) = if (args.first() == "-p") {
            Pair(args[1].toByteArray(), args[2].toByteArray())
        } else {
            Pair(readAndTrim(File(args[0])), readAndTrim(File(args[1])))
        }

        val score = matcher.match(templateOne, templateTwo)
        return if (score >= threshold) {
            logger.log("Match! Score: $score")
            true
        } else {
            logger.log("Not a match. Score: $score")
            false
        }
    }

    private fun readAndTrim(file: File) = String(file.readBytes()).trim().toByteArray()
}