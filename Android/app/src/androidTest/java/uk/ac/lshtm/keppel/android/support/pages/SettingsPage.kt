package uk.ac.lshtm.keppel.android.support.pages

import androidx.test.espresso.matcher.RootMatchers.isDialog
import uk.ac.lshtm.keppel.android.BuildConfig
import uk.ac.lshtm.keppel.android.R
import uk.ac.lshtm.keppel.android.support.Assertions.assertTextDisplayed
import uk.ac.lshtm.keppel.android.support.Assertions.assertTextNotDisplayed
import uk.ac.lshtm.keppel.android.support.Interactions.clickOn

class SettingsPage : Page<SettingsPage> {
    override fun assert(): SettingsPage {
        assertTextDisplayed(R.string.app_name)
        assertTextDisplayed(BuildConfig.VERSION_NAME)
        return this
    }

    fun clickChangeScanner(): ChangeScannerDialogPage {
        clickOn(R.string.change_scanner)
        return ChangeScannerDialogPage().assert()
    }

    fun clickTestScanner(): ConnectingPage {
        clickOn(R.string.test_scanner)
        return ConnectingPage().assert()
    }
}

class ChangeScannerDialogPage : Page<ChangeScannerDialogPage> {
    override fun assert(): ChangeScannerDialogPage {
        assertTextDisplayed(R.string.change_scanner, root = isDialog())
        return this
    }

    fun changeScanner(newScanner: String) {
        clickOn(newScanner, root = isDialog())
    }

    fun assertNoScanner(scanner: String) {
        assertTextNotDisplayed(scanner, root = isDialog())
    }
}
