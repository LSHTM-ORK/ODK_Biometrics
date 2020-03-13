package uk.ac.lshtm.keppel.cli

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.apache.commons.codec.binary.Hex
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test


class MatchCommandTest {

    private val fileOne = createTempFile().apply { writeText("finger-1\n") }
    private val fileTwo = createTempFile().apply { writeText("  finger-2") }
    private val logger = FakeLogger()

    @Test
    fun whenMatchIsLessThanThreshold_andlogsNotAMatch() {
        val matcher = mock<Matcher> {
            on { match(any(), any()) } doReturn 5.0
        }

        val matchCommand = MatchCommand(matcher, 10.0)
        matchCommand.execute(listOf(fileOne.absolutePath, fileTwo.absolutePath), logger)
        assertThat(logger.lines, equalTo(listOf("Not a match. Score: 5.0")))
    }

    @Test
    fun whenEqualToThreshold_logsNotAMatch() {
        val matcher = mock<Matcher> {
            on { match(any(), any()) } doReturn 10.0
        }

        val matchCommand = MatchCommand(matcher, 10.0)
        matchCommand.execute(listOf(fileOne.absolutePath, fileTwo.absolutePath), logger)
        assertThat(logger.lines, equalTo(listOf("Match! Score: 10.0")))
    }

    @Test
    fun whenGreaterThanThreshold_logsNotAMatch() {
        val matcher = mock<Matcher> {
            on { match(any(), any()) } doReturn 11.0
        }

        val matchCommand = MatchCommand(matcher, 10.0)
        matchCommand.execute(listOf(fileOne.absolutePath, fileTwo.absolutePath), logger)
        assertThat(logger.lines, equalTo(listOf("Match! Score: 11.0")))
    }

    @Test
    fun passesTrimmedFileContentToMatcher() {
        val matcher = mock<Matcher> {
            on { match(any(), any()) } doReturn 5.0
        }

        val matchCommand = MatchCommand(matcher, 10.0)
        matchCommand.execute(listOf(fileOne.absolutePath, fileTwo.absolutePath), logger)
        verify(matcher).match("finger-1".toByteArray(), "finger-2".toByteArray())
    }

    @Test
    fun withTwoArguments_returnsTrue() {
        val matcher = mock<Matcher> {
            on { match(any(), any()) } doReturn 5.0
        }

        val matchCommand = MatchCommand(matcher, 0.0)
        assertThat(matchCommand.execute(listOf(fileOne.absolutePath, fileTwo.absolutePath), logger), equalTo(true))
    }

    @Test
    fun withNoArguments_returnsFalse_andLogsHelp() {
        val matcher = mock<Matcher>()
        val matchCommand = MatchCommand(matcher, 10.0)

        assertThat(matchCommand.execute(listOf(), logger), equalTo(false))
        assertThat(logger.lines, equalTo(listOf("Usage: match [TEMPLATE_FILE_1] [TEMPLATE_FILE_2]")))
    }

    @Test
    fun withOneArgument_returnsFalse_andLogsHelp() {
        val matcher = mock<Matcher>()
        val matchCommand = MatchCommand(matcher, 10.0)

        assertThat(matchCommand.execute(listOf(fileOne.absolutePath), logger), equalTo(false))
        assertThat(logger.lines, equalTo(listOf("Usage: match [TEMPLATE_FILE_1] [TEMPLATE_FILE_2]")))
    }

    @Test
    fun withPFlag_whenMatchIsLessThanThreshold_logsScore() {
        val matcher = mock<Matcher> {
            on { match(any(), any()) } doReturn 5.0
        }

        val matchCommand = MatchCommand(matcher, 10.0)
        matchCommand.execute(listOf("-p", Hex.encodeHexString("finger-1".toByteArray()), Hex.encodeHexString("finger-1".toByteArray())), logger)
        assertThat(logger.lines, equalTo(listOf("Not a match. Score: 5.0")))
    }

    @Test
    fun withPFlag_withTwoArguments_returnsTrue() {
        val matcher = mock<Matcher> {
            on { match(any(), any()) } doReturn 5.0
        }

        val matchCommand = MatchCommand(matcher, 10.0)
        assertThat(matchCommand.execute(listOf("-p", Hex.encodeHexString("finger-1".toByteArray()), Hex.encodeHexString("finger-1".toByteArray())), logger), equalTo(true))
    }

    @Test
    fun withPFlag_withNoArguments_returnsFalse_logsHelp() {
        val matcher = mock<Matcher>()
        val matchCommand = MatchCommand(matcher, 10.0)

        assertThat(matchCommand.execute(listOf("-p"), logger), equalTo(false))
        assertThat(logger.lines, equalTo(listOf("Usage: match -p [TEMPLATE_1] [TEMPLATE_2]")))
    }

    @Test
    fun withPFlag_withOneArgument_returnsFalse_andLogsHelp() {
        val matcher = mock<Matcher>()
        val matchCommand = MatchCommand(matcher, 10.0)

        assertThat(matchCommand.execute(listOf("-p", Hex.encodeHexString("finger-1".toByteArray())), logger), equalTo(false))
        assertThat(logger.lines, equalTo(listOf("Usage: match -p [TEMPLATE_1] [TEMPLATE_2]")))
    }
}

private class FakeLogger : Logger {
    private val _lines: ArrayList<String> = arrayListOf()

    val lines: List<String>
        get() = _lines

    override fun log(text: String) {
        _lines.add(text)
    }
}