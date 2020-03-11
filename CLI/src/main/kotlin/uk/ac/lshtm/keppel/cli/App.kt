package uk.ac.lshtm.keppel.cli

import kotlin.system.exitProcess

fun main(args: Array<String>) {
    when {
        args.isEmpty() -> {
            printHelp()
        }

        args[0] == "match" -> {
            val result = MatchCommand(SourceAFISMatcher(), 40.0).execute(args.drop(1), StdoutLogger())
            exitProcess(if (result) { 0 } else { 1 })
        }
    }
}

private fun printHelp() {
    println("Commands:")
    println()
    println("    match    Determine if two (hex encoded) fingerprint templates are from the same finger or not")
}
private class StdoutLogger : Logger {

    override fun log(text: String) {
        println(text)
    }
}
