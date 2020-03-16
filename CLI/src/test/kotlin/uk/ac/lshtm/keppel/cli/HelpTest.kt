package uk.ac.lshtm.keppel.cli

import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import uk.ac.lshtm.keppel.cli.support.FakeLogger
import uk.ac.lshtm.keppel.cli.support.FakeMatcher

class HelpTest {

    private val logger = FakeLogger()
    private val matcher = FakeMatcher()

    @Test
    fun showsSyntax() {
        val app = App(matcher, 10.0)
        app.execute(listOf("-h"), logger)
        assertThat(logger.lines[0], containsString("Usage: keppel [OPTIONS] COMMAND [ARGS]..."))
    }

    @Test
    fun showsCommands() {
        val app = App(matcher, 10.0)
        app.execute(listOf("-h"), logger)
        assertThat(logger.lines[0], containsString("match"))
    }
}