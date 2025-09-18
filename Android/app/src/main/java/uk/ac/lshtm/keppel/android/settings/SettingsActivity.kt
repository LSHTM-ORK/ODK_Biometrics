package uk.ac.lshtm.keppel.android.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceDataStore
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import uk.ac.lshtm.keppel.android.ActivityExt.enableEdgeToEdge
import uk.ac.lshtm.keppel.android.BuildConfig
import uk.ac.lshtm.keppel.android.External
import uk.ac.lshtm.keppel.android.OdkExternalRequest
import uk.ac.lshtm.keppel.android.R
import uk.ac.lshtm.keppel.android.dependencies
import uk.ac.lshtm.keppel.android.scanning.Request
import uk.ac.lshtm.keppel.android.scanning.ScanFragment
import uk.ac.lshtm.keppel.android.scanning.ScanViewModelFactory
import uk.ac.lshtm.keppel.android.scanning.ScannerFactory

class SettingsActivity : AppCompatActivity() {

    private val request by lazy {
        Request.Scan(
            false,
            OdkExternalRequest(
                External.ACTION_SCAN,
                false,
                null,
                mapOf(
                    External.PARAM_RETURN_ISO_TEMPLATE to "template",
                    External.PARAM_RETURN_NFIQ to "nfiq"
                )
            )
        )
    }

    private val viewModelFactory by lazy {
        ScanViewModelFactory(
            this,
            request,
            dependencies()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        this.supportFragmentManager.fragmentFactory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return when (loadFragmentClass(classLoader, className)) {
                    SettingsFragment::class.java -> SettingsFragment(
                        Preferences.get(this@SettingsActivity, dependencies().scanners),
                        dependencies().scanners
                    )

                    ScanFragment::class.java -> ScanFragment(viewModelFactory, request)
                    else -> super.instantiate(classLoader, className)
                }
            }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }
}

class SettingsFragment(
    private val dataStore: PreferenceDataStore,
    private val scanners: List<ScannerFactory>
) : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.preferenceDataStore = dataStore

        setPreferencesFromResource(R.xml.preferences, rootKey)
        setupScannerPreference()
        setupAppVersionPreference()
    }

    private fun setupScannerPreference() {
        val scannerPreference = findPreference<ListPreference>(Preferences.SCANNER)!!
        val entries = scanners.filter { it.isAvailable }.map { it.name }.toTypedArray()
        scannerPreference.entries = entries
        scannerPreference.entryValues = entries

        parentFragmentManager.setFragmentResultListener(
            ScanFragment.REQUEST_SCAN,
            this
        ) { _, result ->
            if (!result.getBoolean(ScanFragment.RESULT_CANCEL)) {
                val intent = result.getParcelable<Intent>(ScanFragment.RESULT_INTENT)!!
                findNavController().navigate(
                    SettingsFragmentDirections.settingsToTestResults(
                        intent.getStringExtra("template")!!,
                        intent.getIntExtra("nfiq", -1)
                    )
                )
            }
        }

        findPreference<Preference>("test_scanner")!!.setOnPreferenceClickListener {
            findNavController().navigate(SettingsFragmentDirections.settingsToTestScanner())
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
