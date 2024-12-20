package uk.ac.lshtm.keppel.cli

import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import uk.ac.lshtm.keppel.cli.support.FakeLogger
import uk.ac.lshtm.keppel.cli.support.FakeMatcher
import uk.ac.lshtm.keppel.cli.support.toHexString

class MatchWithThresholdTest {

    private val logger = FakeLogger()
    private val matcher = FakeMatcher()

    @Test
    fun whenMatchIsLessThanThreshold_logsNotAMatch() {
        val fileOne = createTempFile().apply { writeText("index1".toHexString()) }
        val fileTwo = createTempFile().apply { writeText("index2".toHexString()) }
        matcher.addScore("index1", "index2", 15.0)

        val app = App(matcher, 10.0)
        app.execute(listOf("match", "-m", "-t", "20", fileOne.absolutePath, fileTwo.absolutePath), logger)
        assertThat(logger.lines, equalTo(listOf("mismatch")))
    }

    @Test
    fun whenEqualToThreshold_logsAMatch() {
        val fileOne = createTempFile().apply { writeText("index1".toHexString()) }
        val fileTwo = createTempFile().apply { writeText("index2".toHexString()) }
        matcher.addScore("index1", "index2", 20.0)

        val app = App(matcher, 10.0)
        app.execute(listOf("match", "-m", "-t", "20", fileOne.absolutePath, fileTwo.absolutePath), logger)
        assertThat(logger.lines, equalTo(listOf("match")))
    }

    @Test
    fun whenGreaterThanThreshold_logsAMatch() {
        val fileOne = createTempFile().apply { writeText("index1".toHexString()) }
        val fileTwo = createTempFile().apply { writeText("index2".toHexString()) }
        matcher.addScore("index1", "index2", 21.0)

        val app = App(matcher, 10.0)
        app.execute(listOf("match", "-m", "-t", "20", fileOne.absolutePath, fileTwo.absolutePath), logger)
        assertThat(logger.lines, equalTo(listOf("match")))
    }
}