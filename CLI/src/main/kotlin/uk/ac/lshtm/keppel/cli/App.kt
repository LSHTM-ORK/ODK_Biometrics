package uk.ac.lshtm.keppel.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val logger = StdoutLogger()

    try {
        App(SourceAFISMatcher(), 40.0).execute(args.toList(), logger)
    } catch (e: Exception) {
        logger.log(e.message!!)
        exitProcess(1)
    }
}

class App(private val matcher: Matcher,
          private val threshold: Double) {
    fun execute(args: List<String>, logger: Logger) {
        class Root : CliktCommand() {
            override fun run() = Unit
        }

        Root().subcommands(MatchCommand(matcher, threshold, logger)).parse(args)
    }
}

private class StdoutLogger : Logger {
    override fun log(text: String) {
        println(text)
    }
}
