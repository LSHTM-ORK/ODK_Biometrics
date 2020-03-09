package uk.ac.lshtm.mantra.cli

import java.io.File

class MatchCommand(
        private val matcher: Matcher,
        private val log: (String) -> Unit,
        private val oneFile: String,
        private val twoFile: String) {

    fun execute() {
        val score = matcher.match(readAndTrim(File(oneFile)), readAndTrim(File(twoFile)))

        if (score >= 40) {
            log("Match! Score: $score")
        } else {
            log("Not a match. Score: $score")
        }
    }

    private fun readAndTrim(file: File) = String(file.readBytes()).trim().toByteArray()
}