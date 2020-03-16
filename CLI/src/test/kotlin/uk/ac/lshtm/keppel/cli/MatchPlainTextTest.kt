package uk.ac.lshtm.keppel.cli

import org.apache.commons.codec.binary.Hex
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import uk.ac.lshtm.keppel.cli.support.FakeLogger
import uk.ac.lshtm.keppel.cli.support.FakeMatcher

class MatchPlainTextTest {

    private val logger = FakeLogger()
    private val matcher = FakeMatcher()

    @Test
    fun logsScore() {
        val app = App(matcher, 10.0)
        app.execute(listOf("match", "-p", Hex.encodeHexString("index".toByteArray()), Hex.encodeHexString("index_210".toByteArray())), logger)
        assertThat(logger.lines, equalTo(listOf("210.0")))
    }

    @Test
    fun withOneArgument_throwsException() {
        val app = App(matcher, 10.0)

        app.execute(listOf("match", "-p", Hex.encodeHexString("index".toByteArray())), logger)
        assertThat(logger.lines, equalTo(listOf("Missing argument \"TEMPLATE_TWO\".")))
    }
}