package uk.ac.lshtm.keppel.cli

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import uk.ac.lshtm.keppel.cli.support.FakeLogger
import uk.ac.lshtm.keppel.cli.support.FakeMatcher
import uk.ac.lshtm.keppel.cli.support.toHexString

class MatchWithoutScoreTest {

    private val logger = FakeLogger()
    private val matcher = FakeMatcher()

    @Test
    fun whenMatchIsLessThanThreshold_logsScore() {
        val fileOne = createTempFile().apply { writeText("index1".toHexString()) }
        val fileTwo = createTempFile().apply { writeText("index2".toHexString()) }
        matcher.addScore("index1", "index2", 9.0)

        val app = App(matcher, 10.0)
        app.execute(listOf("match", "-m", fileOne.absolutePath, fileTwo.absolutePath), logger)
        assertThat(logger.lines, equalTo(listOf("mismatch")))
    }

    @Test
    fun whenEqualToThreshold_logsAMatch() {
        val fileOne = createTempFile().apply { writeText("index1".toHexString()) }
        val fileTwo = createTempFile().apply { writeText("index2".toHexString()) }
        matcher.addScore("index1", "index2", 10.0)

        val app = App(matcher, 10.0)
        app.execute(listOf("match", "-m", fileOne.absolutePath, fileTwo.absolutePath), logger)
        assertThat(logger.lines, equalTo(listOf("match")))
    }

    @Test
    fun whenGreaterThanThreshold_logsAMatch() {
        val fileOne = createTempFile().apply { writeText("index1".toHexString()) }
        val fileTwo = createTempFile().apply { writeText("index2".toHexString()) }
        matcher.addScore("index1", "index2", 11.0)

        val app = App(matcher, 10.0)
        app.execute(listOf("match", "-m", fileOne.absolutePath, fileTwo.absolutePath), logger)
        assertThat(logger.lines, equalTo(listOf("match")))
    }

    @Test
    fun withNoArguments_logsMissingArgument() {
        val app = App(matcher, 10.0)
        app.execute(listOf("match", "-m"), logger)
        assertThat(logger.lines, equalTo(listOf("Missing argument \"TEMPLATE_ONE\".")))
    }

    @Test
    fun withOneArgument_logsMissingArgument() {
        val fileOne = createTempFile().apply { writeText("index".toHexString()) }

        val app = App(matcher, 10.0)
        app.execute(listOf("match", "-m", fileOne.absolutePath), logger)
        assertThat(logger.lines, equalTo(listOf("Missing argument \"TEMPLATE_TWO\".")))
    }
}