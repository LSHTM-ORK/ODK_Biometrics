package uk.ac.lshtm.keppel.cli.subject

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test
import uk.ac.lshtm.keppel.cli.support.FakeMatcher
import uk.ac.lshtm.keppel.cli.support.toHexString

class SubjectUseCasesFindMatchTest {

    @Test
    fun `counts scores equal to threshold as matches`() {
        val matcher = FakeMatcher()
        val subjects = listOf(
            Subject("1", listOf("thumb1".toHexString())),
            Subject("2", listOf("thumb2".toHexString()))
        )

        matcher.addScore("thumb1", "thumb2", 10.0)

        val matches = SubjectUseCases.findMatches(subjects, matcher, 10.0)
        assertThat(matches, equalTo(listOf(SubjectUseCases.Match("1", "2", listOf(10.0)))))
    }
}