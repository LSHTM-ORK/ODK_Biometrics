package uk.ac.lshtm.keppel.cli

import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import uk.ac.lshtm.keppel.cli.support.FakeLogger
import uk.ac.lshtm.keppel.cli.support.FakeMatcher

class MatchHelpTest {

    private val logger = FakeLogger()
    private val matcher = FakeMatcher()

    @Test
    fun showsCommandSyntax() {
        val app = App(matcher, 10.0)
        app.execute(listOf("match", "-h"), logger)
        assertThat(logger.lines[0], containsString("Usage: keppel match [OPTIONS] TEMPLATE_ONE TEMPLATE_TWO"))
    }

    @Test
    fun showsOptions() {
        val app = App(matcher, 10.0)
        app.execute(listOf("match", "-h"), logger)
        assertThat(logger.lines[0], allOf(containsString("-p ")))
        assertThat(logger.lines[0], allOf(containsString("-m ")))
        assertThat(logger.lines[0], allOf(containsString("-ms ")))
    }
}