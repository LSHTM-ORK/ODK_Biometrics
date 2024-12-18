package uk.ac.lshtm.keppel.cli

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test
import uk.ac.lshtm.keppel.cli.support.FakeLogger
import uk.ac.lshtm.keppel.cli.support.FakeMatcher
import uk.ac.lshtm.keppel.cli.support.toHexString
import java.io.File

class PMatchCommandTest {

    private val logger = FakeLogger()
    private val matcher = FakeMatcher()

    @Test
    fun `writes output CSV with matched ids`() {
        val inputCsv = createTempFile().apply {
            writeText("""
                id, template_1
                1, ${"index1".toHexString()}
                2, ${"index2".toHexString()}
                3, ${"index3".toHexString()}
            """.trimIndent())
        }

        matcher.addScore("index1", "index2", 5.0)
        matcher.addScore("index1", "index3", 11.0)
        matcher.addScore("index2", "index3", 5.0)

        val app = App(matcher, 10.0)
        val outputFile = File(createTempDir(), "output.csv")
        app.execute(
            listOf(
                "pmatch",
                "-i", inputCsv.absolutePath,
                "-o", outputFile.absolutePath
            ),
            logger
        )

        assertThat(logger.lines, equalTo(emptyList()))

        val output = outputFile.readLines()
        assertThat(output.size, equalTo(2))
        assertThat(output[0], equalTo("id_1, id_2, score_1"))
        assertThat(output[1], equalTo("1, 3, 11.0"))
    }

    @Test
    fun `supports multiple templates per subject`() {
        val inputCsv = createTempFile().apply {
            writeText("""
                id, template_1, template_2
                1, ${"index1".toHexString()}, ${"ring1".toHexString()}
                2, ${"index2".toHexString()}, ${"ring2".toHexString()}
                3, ${"index3".toHexString()}, ${"ring3".toHexString()}
            """.trimIndent())
        }

        matcher.addScore("index1", "index2", 5.0)
        matcher.addScore("index1", "index3", 5.0)
        matcher.addScore("index2", "index3", 5.0)
        matcher.addScore("ring1", "ring2", 5.0)
        matcher.addScore("ring1", "ring3", 5.0)
        matcher.addScore("ring2", "ring3", 11.0)

        val app = App(matcher, 10.0)
        val outputFile = File(createTempDir(), "output.csv")
        app.execute(
            listOf(
                "pmatch",
                "-i", inputCsv.absolutePath,
                "-o", outputFile.absolutePath
            ),
            logger
        )

        assertThat(logger.lines, equalTo(emptyList()))

        val output = outputFile.readLines()
        assertThat(output.size, equalTo(2))
        assertThat(output[0], equalTo("id_1, id_2, score_1"))
        assertThat(output[1], equalTo("2, 3, 11.0"))
    }

    @Test
    fun `logs error when CSV is empty`() {
        val emptyCsv = createTempFile().apply {
            writeText("")
        }

        val app = App(matcher, 10.0)
        app.execute(
            listOf(
                "pmatch",
                "-i", emptyCsv.absolutePath,
                "-o", File(createTempDir(), "output.csv").absolutePath
            ),
            logger
        )

        assertThat(logger.lines, equalTo(listOf(Strings.ERROR_PMATCH_NO_HEADER_ROW)))
    }

    @Test
    fun `logs error when CSV header is incorrect`() {
        val badCsv = createTempFile().apply {
            writeText("blah, other, thing")
        }

        val app = App(matcher, 10.0)
        app.execute(
            listOf(
                "pmatch",
                "-i", badCsv.absolutePath,
                "-o", File(createTempDir(), "output.csv").absolutePath
            ),
            logger
        )

        assertThat(logger.lines, equalTo(listOf(Strings.ERROR_PMATCH_NO_HEADER_ROW)))
    }
}