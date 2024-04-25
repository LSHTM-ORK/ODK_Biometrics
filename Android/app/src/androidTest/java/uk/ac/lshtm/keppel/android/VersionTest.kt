package uk.ac.lshtm.keppel.android

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.lshtm.keppel.android.support.KeppelTestRule

@RunWith(AndroidJUnit4::class)
class VersionTest {

    @get:Rule
    val rule = KeppelTestRule()

    @Test
    fun canSeeAppVersion() {
        rule.launchApp()
            .assertTextDisplayed(BuildConfig.VERSION_NAME)
    }
}
