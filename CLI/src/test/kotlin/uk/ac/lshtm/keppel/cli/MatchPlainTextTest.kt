package uk.ac.lshtm.keppel.cli

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.apache.commons.codec.binary.Hex
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import uk.ac.lshtm.keppel.cli.support.FakeLogger

class MatchPlainTextTest {

    private val logger = FakeLogger()

    @Test
    fun whenMatchIsLessThanThreshold_logsScore() {
        val matcher = mock<Matcher> {
            on { match(any(), any()) } doReturn 5.0
        }

        val app = App(matcher, 10.0)
        app.execute(listOf("match", "-p", Hex.encodeHexString("finger-1".toByteArray()), Hex.encodeHexString("finger-1".toByteArray())), logger)
        assertThat(logger.lines, equalTo(listOf("Not a match. Score: 5.0")))
    }

    @Test
    fun withNoArguments_throwsException() {
        val matcher = mock<Matcher>()
        val app = App(matcher, 10.0)

        app.execute(listOf("match", "-p"), logger)
        assertThat(logger.lines, equalTo(listOf("Missing argument \"TEMPLATE_ONE\".")))
    }

    @Test
    fun withOneArgument_throwsException() {
        val matcher = mock<Matcher>()
        val app = App(matcher, 10.0)

        app.execute(listOf("match", "-p", Hex.encodeHexString("finger-1".toByteArray())), logger)
        assertThat(logger.lines, equalTo(listOf("Missing argument \"TEMPLATE_TWO\".")))
    }
}