package uk.ac.lshtm.keppel.android.support.pages

import androidx.test.espresso.Root
import org.hamcrest.Matcher
import uk.ac.lshtm.keppel.android.support.Assertions

interface Page<T : Page<T>> {

    fun assert(): T
    fun root(): Matcher<Root>? = null

    @Suppress("UNCHECKED_CAST")
    fun assertTextDisplayed(text: String): T {
        Assertions.assertTextDisplayed(text, root())
        return this as T
    }
}
