package uk.ac.lshtm.keppel.cli

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import uk.ac.lshtm.keppel.cli.support.FakeLogger
import kotlin.test.assertFailsWith


class MatchTest {

    private val fileOne = createTempFile().apply { writeText("finger-1\n") }
    private val fileTwo = createTempFile().apply { writeText("  finger-2") }
    private val logger = FakeLogger()

    @Test
    fun whenMatchIsLessThanThreshold_logsNotAMatch() {
        val matcher = mock<Matcher> {
            on { match(any(), any()) } doReturn 5.0
        }

        val app = App(matcher, 10.0)
        app.execute(listOf("match", fileOne.absolutePath, fileTwo.absolutePath), logger)
        assertThat(logger.lines, equalTo(listOf("Not a match. Score: 5.0")))
    }

    @Test
    fun whenEqualToThreshold_logsNotAMatch() {
        val matcher = mock<Matcher> {
            on { match(any(), any()) } doReturn 10.0
        }

        val app = App(matcher, 10.0)
        app.execute(listOf("match", fileOne.absolutePath, fileTwo.absolutePath), logger)
        assertThat(logger.lines, equalTo(listOf("Match! Score: 10.0")))
    }

    @Test
    fun whenGreaterThanThreshold_logsNotAMatch() {
        val matcher = mock<Matcher> {
            on { match(any(), any()) } doReturn 11.0
        }

        val app = App(matcher, 10.0)
        app.execute(listOf("match", fileOne.absolutePath, fileTwo.absolutePath), logger)
        assertThat(logger.lines, equalTo(listOf("Match! Score: 11.0")))
    }

    @Test
    fun passesTrimmedFileContentToMatcher() {
        val matcher = mock<Matcher> {
            on { match(any(), any()) } doReturn 5.0
        }

        val app = App(matcher, 10.0)
        app.execute(listOf("match", fileOne.absolutePath, fileTwo.absolutePath), logger)
        verify(matcher).match("finger-1".toByteArray(), "finger-2".toByteArray())
    }

    @Test
    fun withNoArguments_throwsException() {
        val matcher = mock<Matcher>()
        val app = App(matcher, 10.0)

        val exception = assertFailsWith(Exception::class) {
            app.execute(listOf("match"), logger)
        }
        assertThat(exception.message, equalTo("Missing argument \"TEMPLATE_ONE\"."))
    }

    @Test
    fun withOneArgument_throwsException() {
        val matcher = mock<Matcher>()
        val app = App(matcher, 10.0)

        val exception = assertFailsWith(Exception::class) {
            app.execute(listOf("match", fileOne.absolutePath), logger)
        }
        assertThat(exception.message, equalTo("Missing argument \"TEMPLATE_TWO\"."))
    }
}