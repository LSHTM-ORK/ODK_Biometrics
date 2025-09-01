package uk.ac.lshtm.keppel.cli.util

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test

class ParallelFoldTest {

    @Test
    fun `creates a set from the sets returned from the operation`() {
        val sequence = sequenceOf(1, 2, 3, 4)
        val evens = sequence.parallelFold {
            if (it % 2 == 0) {
                setOf(it)
            } else {
                emptySet()
            }
        }

        assertThat(evens, equalTo(setOf(2, 4)))
    }
}