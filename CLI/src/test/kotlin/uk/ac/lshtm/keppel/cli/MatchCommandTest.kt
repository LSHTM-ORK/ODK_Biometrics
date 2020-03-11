package uk.ac.lshtm.keppel.cli

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test


class MatchCommandTest {

    private val fileOne = createTempFile().apply { writeText("finger-1\n") }
    private val fileTwo = createTempFile().apply { writeText("  finger-2") }

    @Test
    fun execute_whenMatchIsLessThanThreshold_returnsFalseAndLogsScore() {
        val logger = mock<Logger>()
        val matcher = mock<Matcher> {
            on { match(any(), any()) } doReturn 5.0
        }

        val matchCommand = MatchCommand(matcher, 10.0)
        assertThat(matchCommand.execute(listOf(fileOne.absolutePath, fileTwo.absolutePath), logger), equalTo(false))
        verify(logger).log("Not a match. Score: 5.0")
    }

    @Test
    fun execute_whenEqualToThreshold_returnsTrueAndLogsScore() {
        val logger = mock<Logger>()
        val matcher = mock<Matcher> {
            on { match(any(), any()) } doReturn 10.0
        }

        val matchCommand = MatchCommand(matcher, 10.0)
        assertThat(matchCommand.execute(listOf(fileOne.absolutePath, fileTwo.absolutePath), logger), equalTo(true))
        verify(logger).log("Match! Score: 10.0")
    }

    @Test
    fun execute_whenGreaterThanThreshold_returnsTrueAndLogsScore() {
        val logger = mock<Logger>()
        val matcher = mock<Matcher> {
            on { match(any(), any()) } doReturn 11.0
        }

        val matchCommand = MatchCommand(matcher, 10.0)
        assertThat(matchCommand.execute(listOf(fileOne.absolutePath, fileTwo.absolutePath), logger), equalTo(true))
        verify(logger).log("Match! Score: 11.0")
    }

    @Test
    fun execute_passesTrimmedFileContentToMatcher() {
        val logger = mock<Logger>()
        val matcher = mock<Matcher> {
            on { match(any(), any()) } doReturn 5.0
        }

        val matchCommand = MatchCommand(matcher, 10.0)
        matchCommand.execute(listOf(fileOne.absolutePath, fileTwo.absolutePath), logger)
        verify(matcher).match("finger-1".toByteArray(), "finger-2".toByteArray())
    }

    @Test
    fun execute_withNoArguments_returnsFalseAndLogsHelp() {
        val logger = mock<Logger>()
        val matcher = mock<Matcher>()
        val matchCommand = MatchCommand(matcher, 10.0)

        assertThat(matchCommand.execute(listOf(), logger), equalTo(false))
        verify(logger).log("Usage: match [TEMPLATE_FILE_1] [TEMPLATE_FILE_2]")
    }

    @Test
    fun execute_withOneArgument_returnsFalseAndLogsHelp() {
        val logger = mock<Logger>()
        val matcher = mock<Matcher>()
        val matchCommand = MatchCommand(matcher, 10.0)

        assertThat(matchCommand.execute(listOf(fileOne.absolutePath), logger), equalTo(false))
        verify(logger).log("Usage: match [TEMPLATE_FILE_1] [TEMPLATE_FILE_2]")
    }
}