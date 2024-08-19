package uk.ac.lshtm.keppel.android

import android.content.Intent
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import uk.ac.lshtm.keppel.android.support.FakeMatcher
import uk.ac.lshtm.keppel.android.support.FakeScanner
import uk.ac.lshtm.keppel.android.support.FakeScannerFactory
import uk.ac.lshtm.keppel.android.support.KeppelTestRule
import uk.ac.lshtm.keppel.android.support.pages.ConnectingPage
import uk.ac.lshtm.keppel.android.support.pages.MatchPage
import uk.ac.lshtm.keppel.core.toHexString

class MultiMatchAction {

    private val fakeScanner = FakeScanner()
    private val fakeMatcher = FakeMatcher()

    @get:Rule
    val rule = KeppelTestRule(
        scanners = listOf(FakeScannerFactory(fakeScanner)),
        matcher = fakeMatcher
    )

    @Test
    fun capturesAndReturnsMaxMatchScore() {
        fakeMatcher.addScore("blah1", "scanned", 94.0)
        fakeMatcher.addScore("blah2", "scanned", 96.0)
        fakeMatcher.addScore("blah3", "scanned", 95.0)

        val intent = Intent(External.ACTION_MULTI_MATCH).also {
            it.putExtra(OdkExternal.PARAM_INPUT_VALUE, "foo")
            it.putExtra(External.paramIsoTemplate(1), "blah1".toHexString())
            it.putExtra(External.paramIsoTemplate(2), "blah2".toHexString())
            it.putExtra(External.paramIsoTemplate(3) + "_3", "blah3".toHexString())
        }

        val result = rule.launchAction(intent, ConnectingPage()) {
            it.connect(fakeScanner, MatchPage()).clickMatch()
            fakeScanner.returnTemplate("scanned", 1)
        }

        val extras = result.resultData.extras!!
        assertThat(
            extras.getDouble(OdkExternal.PARAM_RETURN_VALUE),
            equalTo(96.0)
        )
    }
}
