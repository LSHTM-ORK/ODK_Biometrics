package uk.ac.lshtm.keppel.cli

fun main(args: Array<String>) {
    MatchCommand(SourceAFISMatcher(), 40.0).execute(args, StdoutLogger())
}

private class StdoutLogger : Logger {

    override fun log(text: String) {
        println(text)
    }
}
