package uk.ac.lshtm.keppel.cli

import com.nhaarman.mockitokotlin2.mock
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import uk.ac.lshtm.keppel.cli.support.FakeLogger

class MatchHelpTest {

    private val logger = FakeLogger()

    @Test
    fun showsCommandSyntax() {
        val app = App(mock(), 10.0)
        app.execute(listOf("match", "-h"), logger)
        assertThat(logger.lines[0], containsString("Usage: keppel match [OPTIONS] TEMPLATE_ONE TEMPLATE_TWO"))
    }

    @Test
    fun showsOptions() {
        val app = App(mock(), 10.0)
        app.execute(listOf("match", "-h"), logger)
        assertThat(logger.lines[0], allOf(containsString("-p")))
    }
}