package uk.ac.lshtm.keppel.android.support.pages

import uk.ac.lshtm.keppel.android.support.Assertions

interface Page<T : Page<T>> {

    fun assert(): T

    @Suppress("UNCHECKED_CAST")
    fun assertTextDisplayed(text: String): T {
        Assertions.assertTextDisplayed(text)
        return this as T
    }
}
