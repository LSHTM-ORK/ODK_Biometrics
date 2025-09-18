package uk.ac.lshtm.keppel.cli.util

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test

class UniquePairsTest {

    @Test
    fun `generates unique pairs from a list`() {
        val list = listOf("1", "2", "3")
        assertThat(
            list.uniquePairs().toList(),
            equalTo(
                listOf(
                    Pair("1", "2"),
                    Pair("1", "3"),
                    Pair("2", "3"),
                )
            )
        )
    }

    @Test
    fun `does not exclude duplicates`() {
        val list = listOf("1", "1", "2")
        assertThat(
            list.uniquePairs().toList(),
            equalTo(
                listOf(
                    Pair("1", "1"),
                    Pair("1", "2"),
                    Pair("1", "2"),
                )
            )
        )
    }
}