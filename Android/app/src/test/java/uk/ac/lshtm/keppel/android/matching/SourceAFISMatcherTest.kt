package uk.ac.lshtm.keppel.android.matching

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test

class SourceAFISMatcherTest {

    @Test
    fun `match returns null when one and two are blank`() {
        val score = SourceAFISMatcher().match("".toByteArray(), "".toByteArray())
        assertThat(score, equalTo(null))
    }

    @Test
    fun `match returns null when one and two are not valid template data`() {
        val score = SourceAFISMatcher().match("one".toByteArray(), "two".toByteArray())
        assertThat(score, equalTo(null))
    }
}
