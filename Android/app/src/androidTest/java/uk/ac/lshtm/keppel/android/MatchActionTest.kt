package uk.ac.lshtm.keppel.android

import android.app.Activity
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import uk.ac.lshtm.keppel.android.support.FakeMatcher
import uk.ac.lshtm.keppel.android.support.FakeScanner
import uk.ac.lshtm.keppel.android.support.FakeScannerFactory
import uk.ac.lshtm.keppel.android.support.KeppelTestRule
import uk.ac.lshtm.keppel.core.toHexString

class MatchActionTest {

    private val fakeScanner = FakeScanner()
    private val fakeMatcher = FakeMatcher()

    @get:Rule
    val rule = KeppelTestRule(
        scanners = listOf(FakeScannerFactory(fakeScanner)),
        matcher = fakeMatcher
    )

    @Test
    fun clickingMatch_capturesAndReturnsMatchScore() {
        val existingTemplate = "blah"

        fakeMatcher.addScore(
            existingTemplate,
            fakeScanner.returnTemplate,
            96.0
        )

        val intent = Intent(OdkExternal.ACTION_MATCH).also {
            it.putExtra(OdkExternal.PARAM_ISO_TEMPLATE, "blah".toHexString())
        }
        val scenario = rule.launchAction(intent)

        onView(withText(R.string.match)).perform(click())
        assertThat(
            scenario.result.resultCode,
            equalTo(Activity.RESULT_OK)
        )

        val extras = scenario.result.resultData.extras!!
        assertThat(
            extras.getDouble(OdkExternal.PARAM_RETURN_VALUE),
            equalTo(96.0)
        )
    }
}
