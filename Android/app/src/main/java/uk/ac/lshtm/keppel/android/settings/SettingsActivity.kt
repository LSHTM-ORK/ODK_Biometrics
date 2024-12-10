package uk.ac.lshtm.keppel.android.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import uk.ac.lshtm.keppel.android.BuildConfig
import uk.ac.lshtm.keppel.android.R
import uk.ac.lshtm.keppel.android.Settings
import uk.ac.lshtm.keppel.android.dependencies
import uk.ac.lshtm.keppel.android.scanning.ScanActivity
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory
import uk.ac.lshtm.keppel.android.settings

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        this.supportFragmentManager.fragmentFactory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return when (loadFragmentClass(classLoader, className)) {
                    SettingsFragment::class.java -> SettingsFragment(
                        settings(),
                        dependencies().scanners
                    )

                    else -> super.instantiate(classLoader, className)
                }
            }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.settings_container, SettingsFragment::
                class.java, null
            )
            .commit()
    }
}

class SettingsFragment(
    private val settings: Settings,
    private val scanners: List<ScannerFactory>
) : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.preferenceDataStore = settings

        setPreferencesFromResource(R.xml.preferences, rootKey)
        setupScannerPreference()
        setupAppVersionPreference()
    }

    private fun setupScannerPreference() {
        val scannerPreference = findPreference<ListPreference>("scanner")!!
        val entries = scanners.filter { it.isAvailable }.map { it.name }.toTypedArray()
        scannerPreference.entries = entries
        scannerPreference.entryValues = entries

        findPreference<Preference>("test_scanner")!!.setOnPreferenceClickListener {
            requireActivity().startActivity(Intent(requireActivity(), ScanActivity::class.java))
            true
        }
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
