package uk.ac.lshtm.keppel.cli

import com.nhaarman.mockitokotlin2.mock
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import uk.ac.lshtm.keppel.cli.support.FakeLogger

class HelpTest {

    private val logger = FakeLogger()

    @Test
    fun showsSyntax() {
        val app = App(mock(), 10.0)
        app.execute(listOf("-h"), logger)
        assertThat(logger.lines[0], containsString("Usage: keppel [OPTIONS] COMMAND [ARGS]..."))
    }

    @Test
    fun showsCommands() {
        val app = App(mock(), 10.0)
        app.execute(listOf("-h"), logger)
        assertThat(logger.lines[0], containsString("match"))
    }
}