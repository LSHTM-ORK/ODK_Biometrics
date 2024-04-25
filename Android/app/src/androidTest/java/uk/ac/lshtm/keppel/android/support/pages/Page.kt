package uk.ac.lshtm.keppel.android.support.pages

interface Page<T : Page<T>> {

    fun assert(): T
}
