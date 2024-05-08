package uk.ac.lshtm.keppel.android.scanning

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.lshtm.keppel.android.OdkExternal

@RunWith(AndroidJUnit4::class)
class IntentParserTest {

    @Test
    fun `fast is false when param is false string`() {
        val intent = Intent().also {
            it.putExtra(OdkExternal.PARAM_FAST, "false")
        }

        val request = IntentParser.parse(intent)
        assertThat(request.fast, equalTo(false))
    }

    @Test
    fun `fast is false when param is arbitrary string`() {
        val intent = Intent().also {
            it.putExtra(OdkExternal.PARAM_FAST, "blah")
        }

        val request = IntentParser.parse(intent)
        assertThat(request.fast, equalTo(false))
    }
}
