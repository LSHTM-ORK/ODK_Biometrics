package uk.ac.lshtm.keppel.cli

interface Command {
    fun execute(args: List<String>, logger: Logger): Boolean
}
