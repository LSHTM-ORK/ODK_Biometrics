package uk.ac.lshtm.keppel.android.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import uk.ac.lshtm.keppel.android.BuildConfig
import uk.ac.lshtm.keppel.android.R
import uk.ac.lshtm.keppel.android.availableScanners

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, SettingsFragment())
            .commit()
    }
}

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        setupScannerPreference()
        setupAppVersionPreference()
    }

    private fun setupScannerPreference() {
        val scannerPreference = findPreference<ListPreference>("scanner")!!
        val entries = availableScanners().map { it.name }.toTypedArray()
        scannerPreference.entries = entries
        scannerPreference.entryValues = entries
    }

    private fun setupAppVersionPreference() {
        val appVersionPreference = findPreference<Preference>("app_version")!!
        appVersionPreference.summary = BuildConfig.VERSION_NAME
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return if (preference.key == "open_source") {
            OssLicensesMenuActivity.setActivityTitle(getString(R.string.open_source_libraries))
            startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            true
        } else {
            super.onPreferenceTreeClick(preference)
        }
    }
}
