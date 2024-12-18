package uk.ac.lshtm.keppel.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.PrintHelpMessage
import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) {
    App(SourceAFISMatcher(), 40.0).execute(args.toList(), StdoutLogger())
}

class App(private val matcher: Matcher,
          private val defaultThreshold: Double) {
    fun execute(args: List<String>, logger: Logger) {
        class Root : CliktCommand(name = "keppel") {
            override fun run() = Unit
        }

        try {
            Root().subcommands(
                MatchCommand(matcher, defaultThreshold, logger),
                PMatch(matcher, defaultThreshold)
            ).parse(args)
        } catch (e: PrintHelpMessage) {
            logger.log(e.command.getFormattedHelp())
        } catch (e: Exception) {
            if (e.message != null) logger.log(e.message!!)
        }
    }
}

private class StdoutLogger : Logger {
    override fun log(text: String) {
        println(text)
    }
}
