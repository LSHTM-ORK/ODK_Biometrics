package uk.ac.lshtm.keppel.android.support.pages

import uk.ac.lshtm.keppel.android.R
import uk.ac.lshtm.keppel.android.support.Assertions.assertTextDisplayed
import uk.ac.lshtm.keppel.android.support.FakeScanner

class ConnectingPage : Page<ConnectingPage> {
    override fun assert(): ConnectingPage {
        assertTextDisplayed(R.string.connect_scanner)
        return this
    }

    fun <T : Page<T>> connect(fakeScanner: FakeScanner, destination: T): T {
        fakeScanner.connect()
        return destination.assert()
    }
}
