package uk.ac.lshtm.keppel.cli

import org.apache.commons.codec.binary.Hex
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test
import uk.ac.lshtm.keppel.cli.support.FakeLogger
import uk.ac.lshtm.keppel.cli.support.FakeMatcher
import uk.ac.lshtm.keppel.cli.support.toHexString
import java.io.File

class PMatchTest {

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
        matcher.addScore("index2", "index3", 9.0)

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
}