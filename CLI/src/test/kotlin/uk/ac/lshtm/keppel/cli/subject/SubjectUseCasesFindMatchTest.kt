package uk.ac.lshtm.keppel.cli.subject

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test
import uk.ac.lshtm.keppel.cli.support.FakeMatcher
import uk.ac.lshtm.keppel.cli.support.toHexString

class SubjectUseCasesFindMatchTest {

    @Test
    fun `returns max score when there are multiple matches between subjects`() {
        val matcher = FakeMatcher()
        val subjects = listOf(
            Subject("1", listOf("thumb1".toHexString(), "index1".toHexString())),
            Subject("2", listOf("thumb2".toHexString(), "index2".toHexString()))
        )

        matcher.addScore("thumb1", "thumb2", 12.0)
        matcher.addScore("index1", "index2", 11.0)

        val matches = SubjectUseCases.findMatches(subjects, matcher, 10.0)
        assertThat(matches, equalTo(listOf(SubjectUseCases.Match("1", "2", 12.0))))
    }
}