package uk.ac.lshtm.keppel.cli.util

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.lessThan
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test
import kotlin.system.measureTimeMillis

class ParallelFoldTest {

    @Test
    fun `creates a set from the sets returned from the operation`() {
        val list = listOf(1, 2, 3, 4)
        val evens = list.parallelFold {
            if (it % 2 == 0) {
                setOf(it)
            } else {
                emptySet()
            }
        }

        assertThat(evens, equalTo(setOf(2, 4)))
    }

    @Test
    fun `beats linear time`() {
        val list = listOf(1, 2, 3, 4, 5)

        val executionTime = measureTimeMillis {
            list.parallelFold {
                Thread.sleep(10)
                setOf(it)
            }
        }

        // Linear execution would be at least 50ms
        assertThat(executionTime, lessThan(50))
    }
}