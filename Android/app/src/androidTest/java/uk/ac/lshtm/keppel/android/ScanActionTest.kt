package uk.ac.lshtm.keppel.android

import android.app.Activity
import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.lshtm.keppel.android.support.FakeScanner
import uk.ac.lshtm.keppel.android.support.FakeScannerFactory
import uk.ac.lshtm.keppel.android.support.KeppelTestRule
import uk.ac.lshtm.keppel.android.support.pages.CapturePage
import uk.ac.lshtm.keppel.android.support.pages.CapturingPage
import uk.ac.lshtm.keppel.android.support.pages.ConnectingPage
import uk.ac.lshtm.keppel.android.support.pages.ErrorDialogPage
import uk.ac.lshtm.keppel.core.toHexString

@RunWith(AndroidJUnit4::class)
class ScanActionTest {

    private val fakeScanner = FakeScanner()

    @get:Rule
    val rule = KeppelTestRule(scanners = listOf(FakeScannerFactory(fakeScanner)))

    @Test
    fun clickingCapture_capturesAndReturnsIsoTemplate() {
        val intent = Intent(External.ACTION_SCAN).also {
            it.putExtra(OdkExternal.PARAM_INPUT_VALUE, "foo")
        }

        val result = rule.launchAction(intent, ConnectingPage()) {
            it.connect(fakeScanner, CapturePage()).clickCapture()
            fakeScanner.returnTemplate("scanned", 1)
        }

        assertThat(result.resultCode, equalTo(Activity.RESULT_OK))
        val extras = result.resultData.extras!!
        assertThat(
            extras.getString(OdkExternal.PARAM_RETURN_VALUE),
            equalTo("scanned".toHexString())
        )
    }

    @Test
    fun clickingCapture_whenReturnValuesSpecified_capturesAndReturnsThoseValues() {
        val intent = Intent(External.ACTION_SCAN).also {
            it.putExtra(External.PARAM_RETURN_ISO_TEMPLATE, "my_iso_template")
            it.putExtra(External.PARAM_RETURN_NFIQ, "my_nfiq")
        }

        val result = rule.launchAction(intent, ConnectingPage()) {
            it.connect(fakeScanner, CapturePage()).clickCapture()
            fakeScanner.returnTemplate("scanned", 17)
        }

        assertThat(result.resultCode, equalTo(Activity.RESULT_OK))
        val extras = result.resultData.extras!!
        assertThat(extras.get("my_iso_template"), equalTo("scanned".toHexString()))
        assertThat(extras.get("my_nfiq"), equalTo(17))
    }

    @Test
    fun clickingCapture_andThenCancel_returnsCancelledResult() {
        val intent = Intent(External.ACTION_SCAN).also {
            it.putExtra(OdkExternal.PARAM_INPUT_VALUE, "foo")
        }

        val result = rule.launchAction(intent, ConnectingPage()) {
            it.connect(fakeScanner, CapturePage())
                .clickCapture()
                .clickCancel()
        }

        assertThat(result.resultCode, equalTo(Activity.RESULT_CANCELED))
        assertThat(result.resultData, equalTo(null))
    }

    @Test
    fun withFastMode_capturesAndReturnsIsoTemplate() {
        val intent = Intent(External.ACTION_SCAN).also {
            it.putExtra(OdkExternal.PARAM_INPUT_VALUE, "foo")
            it.putExtra(External.PARAM_FAST, "true")
        }

        val result = rule.launchAction(intent, ConnectingPage()) {
            it.connect(fakeScanner, CapturingPage())
            fakeScanner.returnTemplate("scanned", 1)
        }

        assertThat(result.resultCode, equalTo(Activity.RESULT_OK))
        val extras = result.resultData.extras!!
        assertThat(
            extras.getString(OdkExternal.PARAM_RETURN_VALUE),
            equalTo("scanned".toHexString())
        )
    }

    @Test
    fun withFastMode_whenCapturingFails_showsAnError() {
        val intent = Intent(External.ACTION_SCAN).also {
            it.putExtra(OdkExternal.PARAM_INPUT_VALUE, "foo")
            it.putExtra(External.PARAM_FAST, "true")
        }

        val result = rule.launchAction(intent, ConnectingPage()) {
            it.connect(fakeScanner, CapturingPage())
            fakeScanner.failToCapture()

            val errorDialog = ErrorDialogPage(R.string.no_capture_result_error).assert()
            assertThat(fakeScanner.capturing, equalTo(false))
            errorDialog.clickOk()
        }

        assertThat(result.resultCode, equalTo(Activity.RESULT_CANCELED))
    }

    @Test
    fun whenScannerFailsToConnect_showsAnError() {
        val intent = Intent(External.ACTION_SCAN).also {
            it.putExtra(OdkExternal.PARAM_INPUT_VALUE, "foo")
            it.putExtra(External.PARAM_FAST, "true")
        }

        val result = rule.launchAction(intent, ConnectingPage()) {
            it.failToConnect(fakeScanner, ErrorDialogPage(R.string.connection_failure_error))
                .clickOk()
        }

        assertThat(result.resultCode, equalTo(Activity.RESULT_CANCELED))
    }
}
