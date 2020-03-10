package uk.ac.lshtm.keppel.cli

fun main(args: Array<String>) {
    MatchCommand(SourceAFISMatcher(), ::println, args[0], args[1]).execute()
}
