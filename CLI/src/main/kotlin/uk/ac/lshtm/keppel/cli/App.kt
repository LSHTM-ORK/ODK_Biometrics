package uk.ac.lshtm.keppel.cli

import kotlin.system.exitProcess

class App(private val matcher: Matcher,
          private val threshold: Double) : Command {
    override fun execute(args: List<String>, logger: Logger) {
        return when {
            args.isEmpty() -> {
                printHelp(logger)
                throw Exception("")
            }

            args[0] == "match" -> {
                MatchCommand(matcher, threshold).execute(args.drop(1), logger)
            }

            else -> {
                printHelp(logger)
                throw Exception("")
            }
        }
    }

    private fun printHelp(logger: Logger) {
        logger.log("Commands:")
        logger.log("    match    Determine if two (hex encoded) fingerprint templates are from the same finger or not")
    }
}

fun main(args: Array<String>) {
    val logger = StdoutLogger()

    try {
        App(SourceAFISMatcher(), 40.0).execute(args.toList(), logger)
    } catch (e: Exception) {
        logger.log(e.message!!)
        exitProcess(1)
    }
}

private class StdoutLogger : Logger {

    override fun log(text: String) {
        println(text)
    }
}
