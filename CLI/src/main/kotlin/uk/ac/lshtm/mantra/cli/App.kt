package uk.ac.lshtm.mantra.cli

fun main(args: Array<String>) {
    MatchCommand(SourceAFISMatcher(), ::println, args[0], args[1]).execute()
}
