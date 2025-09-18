package uk.ac.lshtm.keppel.cli.subject

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test

class SubjectParserTest {

    @Test
    fun `ignores whitespace in columns`() {
        val csv = createTempFile().apply {
            writeText(
                """
                id,template_1, template_2
                1 , blah , other
            """.trimIndent()
            )
        }

        val subjects = SubjectParser.parseCsv(csv.absolutePath)
        assertThat(subjects, equalTo(listOf(Subject("1", listOf("blah", "other")))))
    }
}