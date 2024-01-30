package uk.ac.lshtm.keppel.android

import android.app.Activity
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.lshtm.keppel.android.support.KeppelTestRule
import uk.ac.lshtm.keppel.core.toHexString

@RunWith(AndroidJUnit4::class)
class ScanActionTest {

    @get:Rule
    val rule = KeppelTestRule()

    @Test
    fun clickingCapture_capturesAndReturnsIsoTemplate() {
        val intent = Intent(OdkExternal.ACTION_SCAN).also {
            it.putExtra(OdkExternal.PARAM_INPUT_VALUE, "foo")
        }
        val scenario = rule.launchAction(intent)

        onView(withText(R.string.capture)).perform(click())
        assertThat(scenario.result.resultCode, equalTo(Activity.RESULT_OK))
        val extras = scenario.result.resultData.extras!!
        assertThat(
            extras.getString(OdkExternal.PARAM_RETURN_VALUE),
            equalTo("ISO TEMPLATE".toHexString())
        )
    }

    @Test
    fun clickingCapture_whenReturnValuesSpecified_capturesAndReturnsThoseValues_fromScanner() {
        val intent = Intent(OdkExternal.ACTION_SCAN).also {
            it.putExtra(OdkExternal.PARAM_RETURN_ISO_TEMPLATE, "my_iso_template")
            it.putExtra(OdkExternal.PARAM_RETURN_NFIQ, "my_nfiq")
        }
        val scenario = rule.launchAction(intent)

        onView(withText(R.string.capture)).perform(click())
        assertThat(scenario.result.resultCode, equalTo(Activity.RESULT_OK))
        val extras = scenario.result.resultData.extras!!
        assertThat(
            extras.get("my_iso_template"),
            equalTo("ISO TEMPLATE".toHexString())
        )
        assertThat(extras.get("my_nfiq"), equalTo(17))
    }

    @Test
    fun clickingCapture_whenOnlyOneReturnValueSpecified_capturesAndReturnsThatValue_fromScanner() {
        val intent = Intent(OdkExternal.ACTION_SCAN).also {
            it.putExtra(OdkExternal.PARAM_RETURN_NFIQ, "my_nfiq")
        }
        val scenario = rule.launchAction(intent)

        onView(withText(R.string.capture)).perform(click())
        assertThat(scenario.result.resultCode, equalTo(Activity.RESULT_OK))
        val extras = scenario.result.resultData.extras!!
        assertThat(extras.size(), equalTo(1))
        assertThat(extras.get("my_nfiq"), equalTo(17))
    }
}
