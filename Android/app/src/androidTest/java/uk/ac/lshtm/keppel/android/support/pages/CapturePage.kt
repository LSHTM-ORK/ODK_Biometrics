package uk.ac.lshtm.keppel.android.support.pages

import uk.ac.lshtm.keppel.android.R
import uk.ac.lshtm.keppel.android.support.Assertions.assertTextDisplayed
import uk.ac.lshtm.keppel.android.support.Assertions.assertTextNotDisplayed
import uk.ac.lshtm.keppel.android.support.Interactions.clickOn

class CapturePage : Page<CapturePage> {
    override fun assert(): CapturePage {
        assertTextDisplayed(R.string.capture)
        assertTextNotDisplayed(R.string.connect_scanner)
        assertTextNotDisplayed(R.string.place_finger)
        return this
    }

    fun clickCapture(): CapturingPage {
        clickOn(R.string.capture)
        return CapturingPage().assert()
    }
}
