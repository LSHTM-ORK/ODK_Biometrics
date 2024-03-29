package uk.ac.lshtm.keppel.android

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OdkExternalTest {

    @Test
    fun `buildMultipleReturnResult does not include result when key is not in input intent`() {
        val inputIntent = Intent().also {
            it.putExtra("my_return_param", "my_return_field")
        }
        val results =
            mapOf("my_return_param" to "result", "my_other_return_param" to "other result")
        val intent =
            OdkExternal.buildMultipleReturnResult(inputIntent, results)
        assertThat(intent.extras!!.size(), equalTo(1))
        assertThat(intent.extras!!.getString("my_return_field"), equalTo("result"))
    }

    @Test
    fun `buildMultipleReturnResult can include double results`() {
        val inputIntent = Intent().also {
            it.putExtra("my_return_param", "my_return_field")
        }
        val results =
            mapOf("my_return_param" to 12.0)
        val intent =
            OdkExternal.buildMultipleReturnResult(inputIntent, results)
        assertThat(intent.extras!!.getDouble("my_return_field"), equalTo(12.0))
    }

    @Test
    fun `buildMultipleReturnResult can include int results`() {
        val inputIntent = Intent().also {
            it.putExtra("my_return_param", "my_return_field")
        }
        val results =
            mapOf("my_return_param" to 12)
        val intent =
            OdkExternal.buildMultipleReturnResult(inputIntent, results)
        assertThat(intent.extras!!.getInt("my_return_field"), equalTo(12))
    }
}
