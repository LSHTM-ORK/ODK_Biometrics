package uk.ac.lshtm.keppel.android

import android.app.Activity
import android.content.Intent
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import uk.ac.lshtm.keppel.android.support.FakeMatcher
import uk.ac.lshtm.keppel.android.support.FakeScanner
import uk.ac.lshtm.keppel.android.support.FakeScannerFactory
import uk.ac.lshtm.keppel.android.support.KeppelTestRule
import uk.ac.lshtm.keppel.android.support.pages.ErrorDialogPage
import uk.ac.lshtm.keppel.android.support.pages.MatchPage
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
    fun whenNoTemplateIsSupplied_showsError() {
        val intent = Intent(OdkExternal.ACTION_MATCH).also {
            it.putExtra(OdkExternal.PARAM_INPUT_VALUE, "foo")
            it.putExtra("blah", "blah")
        }

        val result = rule.launchAction(
            intent,
            ErrorDialogPage(R.string.input_missing_error, OdkExternal.PARAM_ISO_TEMPLATE)
        ) {
            it.clickOk()
        }

        assertThat(result.resultCode, equalTo(Activity.RESULT_CANCELED))
    }

    @Test
    fun clickingMatch_capturesAndReturnsMatchScore() {
        val existingTemplate = "blah"
        fakeMatcher.addScore(
            existingTemplate,
            fakeScanner.returnTemplate,
            96.0
        )

        val intent = Intent(OdkExternal.ACTION_MATCH).also {
            it.putExtra(OdkExternal.PARAM_INPUT_VALUE, "foo")
            it.putExtra(OdkExternal.PARAM_ISO_TEMPLATE, existingTemplate.toHexString())
        }

        val result = rule.launchAction(intent, MatchPage()) {
            it.clickMatch()
        }

        assertThat(result.resultCode, equalTo(Activity.RESULT_OK))

        val extras = result.resultData.extras!!
        assertThat(
            extras.getDouble(OdkExternal.PARAM_RETURN_VALUE),
            equalTo(96.0)
        )
    }

    @Test
    fun clickingMatch_whenExistingTemplateIsNotHexEncoded_showsAnError() {
        val existingTemplate = "blah"
        fakeMatcher.addScore(
            existingTemplate,
            fakeScanner.returnTemplate,
            96.0
        )

        val intent = Intent(OdkExternal.ACTION_MATCH).also {
            it.putExtra(OdkExternal.PARAM_INPUT_VALUE, "foo")
            it.putExtra(OdkExternal.PARAM_ISO_TEMPLATE, existingTemplate)
        }

        val result = rule.launchAction(intent, MatchPage()) {
            it.clickMatch(ErrorDialogPage(R.string.input_format_error))
                .clickOk()
        }

        assertThat(result.resultCode, equalTo(Activity.RESULT_CANCELED))
    }

    @Test
    fun clickingMatch_whenMatchFails_showsAnError() {
        val existingTemplate = "blah"
        fakeMatcher.addScore(
            existingTemplate,
            fakeScanner.returnTemplate,
            null
        )

        val intent = Intent(OdkExternal.ACTION_MATCH).also {
            it.putExtra(OdkExternal.PARAM_INPUT_VALUE, "foo")
            it.putExtra(OdkExternal.PARAM_ISO_TEMPLATE, existingTemplate.toHexString())
        }

        val result = rule.launchAction(intent, MatchPage()) {
            it.clickMatch(ErrorDialogPage(R.string.input_format_error))
                .clickOk()
        }

        assertThat(result.resultCode, equalTo(Activity.RESULT_CANCELED))
    }

    @Test
    fun clickingMatch_whenReturnValuesSpecified_capturesAndReturnsThoseValues() {
        val existingTemplate = "blah"
        fakeMatcher.addScore(
            existingTemplate,
            fakeScanner.returnTemplate,
            96.0
        )

        val intent = Intent(OdkExternal.ACTION_MATCH).also {
            it.putExtra(OdkExternal.PARAM_ISO_TEMPLATE, existingTemplate.toHexString())
            it.putExtra(OdkExternal.PARAM_RETURN_SCORE, "my_score")
            it.putExtra(OdkExternal.PARAM_RETURN_ISO_TEMPLATE, "my_iso_template")
            it.putExtra(OdkExternal.PARAM_RETURN_NFIQ, "my_nfiq")
        }

        val result = rule.launchAction(intent, MatchPage()) {
            it.clickMatch()
        }

        assertThat(result.resultCode, equalTo(Activity.RESULT_OK))

        val extras = result.resultData.extras!!
        assertThat(extras.getDouble("my_score"), equalTo(96.0))
        assertThat(extras.getInt("my_nfiq"), equalTo(17))
        assertThat(
            extras.getString("my_iso_template"),
            equalTo(fakeScanner.returnTemplate.toHexString())
        )
    }

    @Test
    fun withFastMode_capturesAndReturnsMatchScore() {
        val existingTemplate = "blah"
        fakeMatcher.addScore(
            existingTemplate,
            fakeScanner.returnTemplate,
            96.0
        )

        val intent = Intent(OdkExternal.ACTION_MATCH).also {
            it.putExtra(OdkExternal.PARAM_INPUT_VALUE, "foo")
            it.putExtra(OdkExternal.PARAM_ISO_TEMPLATE, existingTemplate.toHexString())
            it.putExtra(OdkExternal.PARAM_FAST, "true")
        }

        val result = rule.launchAction(intent)
        assertThat(result.resultCode, equalTo(Activity.RESULT_OK))

        val extras = result.resultData.extras!!
        assertThat(
            extras.getDouble(OdkExternal.PARAM_RETURN_VALUE),
            equalTo(96.0)
        )
    }
}
