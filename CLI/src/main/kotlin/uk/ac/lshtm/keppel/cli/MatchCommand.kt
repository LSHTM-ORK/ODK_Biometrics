package uk.ac.lshtm.keppel.cli

import java.io.File

class MatchCommand(
        private val matcher: Matcher,
        private val threshold: Double) : Command {

    override fun execute(args: List<String>, logger: Logger) {
        if (args.isEmpty()) {
            throw Exception("Usage: match [TEMPLATE_FILE_1] [TEMPLATE_FILE_2]")
        }

        val (templateOne, templateTwo) = if (args.first() == "-p") {
            if (args.size < 3) {
                throw Exception("Usage: match -p [TEMPLATE_1] [TEMPLATE_2]")
            }

            Pair(args[1].toByteArray(), args[2].toByteArray())
        } else {
            if (args.size < 2) {
                throw Exception("Usage: match [TEMPLATE_FILE_1] [TEMPLATE_FILE_2]")
            }

            Pair(readAndTrim(File(args[0])), readAndTrim(File(args[1])))
        }

        val score = matcher.match(templateOne, templateTwo)
        if (score >= threshold) {
            logger.log("Match! Score: $score")
        } else {
            logger.log("Not a match. Score: $score")
        }
    }

    private fun readAndTrim(file: File) = String(file.readBytes()).trim().toByteArray()
}