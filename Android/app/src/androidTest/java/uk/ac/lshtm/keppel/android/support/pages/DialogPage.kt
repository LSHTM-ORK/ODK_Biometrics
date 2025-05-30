package uk.ac.lshtm.keppel.android.support.pages

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Root
import androidx.test.espresso.matcher.RootMatchers.isDialog
import org.hamcrest.Matcher
import uk.ac.lshtm.keppel.android.R
import uk.ac.lshtm.keppel.android.support.Assertions.assertTextDisplayed
import uk.ac.lshtm.keppel.android.support.Interactions.clickOn

class DialogPage(private val text: String) : Page<DialogPage> {

    constructor(
        text: Int,
        vararg formatArgs: Any
    ) : this(ApplicationProvider.getApplicationContext<Application>().getString(text, *formatArgs))

    override fun assert(): DialogPage {
        assertTextDisplayed(text, root = isDialog())
        return this
    }

    override fun root(): Matcher<Root>? {
        return isDialog()
    }

    fun clickOk() {
        clickOn(R.string.ok, root = isDialog())
    }

    fun <D : Page<D>> clickOk(destination: D): D {
        clickOk()
        return destination.assert()
    }
}
