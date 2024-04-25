package uk.ac.lshtm.keppel.android.support.pages

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.RootMatchers.isDialog
import uk.ac.lshtm.keppel.android.R
import uk.ac.lshtm.keppel.android.support.Assertions.assertTextDisplayed
import uk.ac.lshtm.keppel.android.support.Interactions.clickOn

class ErrorDialogPage(private val text: String) : Page<ErrorDialogPage> {

    constructor(
        text: Int,
        vararg formatArgs: Any
    ) : this(ApplicationProvider.getApplicationContext<Application>().getString(text, *formatArgs))

    override fun assert(): ErrorDialogPage {
        assertTextDisplayed(text, root = isDialog())
        return this
    }

    fun clickOk() {
        clickOn(R.string.ok, root = isDialog())
    }
}
