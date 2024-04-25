package uk.ac.lshtm.keppel.android.support.pages

import uk.ac.lshtm.keppel.android.R
import uk.ac.lshtm.keppel.android.support.Assertions.assertTextDisplayed
import uk.ac.lshtm.keppel.android.support.Interactions.clickOn

class CapturePage : Page<CapturePage> {
    override fun assert(): CapturePage {
        assertTextDisplayed(R.string.capture)
        return this
    }

    fun clickCapture() {
        clickOn(R.string.capture)
    }

    fun clickCancel() {
        clickOn(R.string.cancel)
    }
}
