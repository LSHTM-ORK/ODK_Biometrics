package uk.ac.lshtm.keppel.android.support.pages

import uk.ac.lshtm.keppel.android.R
import uk.ac.lshtm.keppel.android.support.Assertions.assertTextDisplayed
import uk.ac.lshtm.keppel.android.support.Interactions.clickOn

class MatchPage : Page<MatchPage> {
    override fun assert(): MatchPage {
        assertTextDisplayed(R.string.match)
        return this
    }

    fun clickMatch() {
        clickOn(R.string.match)
    }

    fun <T : Page<T>> clickMatch(page: T): T {
        clickMatch()
        return page.assert()
    }
}
