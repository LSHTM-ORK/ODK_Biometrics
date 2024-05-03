package uk.ac.lshtm.keppel.android.support.pages

import uk.ac.lshtm.keppel.android.R
import uk.ac.lshtm.keppel.android.support.Assertions.assertTextDisplayed
import uk.ac.lshtm.keppel.android.support.Assertions.assertTextNotDisplayed
import uk.ac.lshtm.keppel.android.support.Interactions.clickOn

class CapturingPage : Page<CapturingPage> {
    override fun assert(): CapturingPage {
        assertTextDisplayed(R.string.place_finger)
        assertTextNotDisplayed(R.string.capture)
        assertTextNotDisplayed(R.string.match)
        return this
    }

    fun clickCancel() {
        clickOn(R.string.cancel)
    }
}
