package uk.ac.lshtm.keppel.cli

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import uk.ac.lshtm.keppel.cli.support.FakeLogger


class MatchWithScoreTest {

    private val fileOne = createTempFile().apply { writeText("finger-1\n") }
    private val fileTwo = createTempFile().apply { writeText("  finger-2") }
    private val logger = FakeLogger()

    @Test
    fun whenMatchIsLessThanThreshold_logsScore() {
        val matcher = mock<Matcher> {
            on { match(any(), any()) } doReturn 5.0
        }

        val app = App(matcher, 10.0)
        app.execute(listOf("match", "-ms", fileOne.absolutePath, fileTwo.absolutePath), logger)
        assertThat(logger.lines, equalTo(listOf("mismatch_5.0")))
    }

    @Test
    fun whenEqualToThreshold_logsNotAMatch() {
        val matcher = mock<Matcher> {
            on { match(any(), any()) } doReturn 10.0
        }

        val app = App(matcher, 10.0)
        app.execute(listOf("match", "-ms", fileOne.absolutePath, fileTwo.absolutePath), logger)
        assertThat(logger.lines, equalTo(listOf("match_10.0")))
    }

    @Test
    fun whenGreaterThanThreshold_logsNotAMatch() {
        val matcher = mock<Matcher> {
            on { match(any(), any()) } doReturn 11.0
        }

        val app = App(matcher, 10.0)
        app.execute(listOf("match", "-ms", fileOne.absolutePath, fileTwo.absolutePath), logger)
        assertThat(logger.lines, equalTo(listOf("match_11.0")))
    }

    @Test
    fun passesTrimmedFileContentToMatcher() {
        val matcher = mock<Matcher> {
            on { match(any(), any()) } doReturn 5.0
        }

        val app = App(matcher, 10.0)
        app.execute(listOf("match", "-ms", fileOne.absolutePath, fileTwo.absolutePath), logger)
        verify(matcher).match("finger-1".toByteArray(), "finger-2".toByteArray())
    }

    @Test
    fun withNoArguments_logsMissingArgument() {
        val matcher = mock<Matcher>()
        val app = App(matcher, 10.0)

        app.execute(listOf("match", "-ms"), logger)
        assertThat(logger.lines, equalTo(listOf("Missing argument \"TEMPLATE_ONE\".")))
    }

    @Test
    fun withOneArgument_logsMissingArgument() {
        val matcher = mock<Matcher>()
        val app = App(matcher, 10.0)

        app.execute(listOf("match", "-ms", fileOne.absolutePath), logger)
        assertThat(logger.lines, equalTo(listOf("Missing argument \"TEMPLATE_TWO\".")))
    }
}