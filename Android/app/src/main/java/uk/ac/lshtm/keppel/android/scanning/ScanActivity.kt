package uk.ac.lshtm.keppel.android.scanning

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.fragment.NavHostFragment
import uk.ac.lshtm.keppel.android.databinding.ActivityScanBinding
import uk.ac.lshtm.keppel.android.dependencies
import uk.ac.lshtm.keppel.android.settings.Preferences
import uk.ac.lshtm.keppel.core.Matcher
import uk.ac.lshtm.keppel.core.Scanner
import uk.ac.lshtm.keppel.core.TaskRunner

class ScanActivity : AppCompatActivity() {

    private val request: Request by lazy { IntentParser.parse(intent) }
    private val viewModelFactory by lazy {
        val scanners = dependencies().scanners
        val settings = Preferences.get(this, scanners)
        val scannerFactory = scanners.first {
            it.name == settings.getString(Preferences.SCANNER, null)
        }.create(this)

        ScanViewModelFactory(
            scannerFactory,
            dependencies().matcher,
            dependencies().taskRunner,
            request
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return when (loadFragmentClass(classLoader, className)) {
                    ScanFragment::class.java -> ScanFragment(viewModelFactory, request)
                    else -> super.instantiate(classLoader, className)
                }
            }
        }

        super.onCreate(savedInstanceState)
        val binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navFragmentManager =
            binding.navHostFragment.getFragment<NavHostFragment>().childFragmentManager
        navFragmentManager.setFragmentResultListener(ScanFragment.REQUEST_SCAN, this) { _, result ->
            returnResult(result.getParcelable(ScanFragment.RESULT_INTENT)!!)
        }
    }

    private fun returnResult(result: Intent) {
        setResult(RESULT_OK, result)
        finish()
    }
}

class ScanViewModelFactory(
    private val scanner: Scanner,
    private val matcher: Matcher,
    private val taskRunner: TaskRunner,
    private val request: Request
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val inputTemplates = request.let {
            if (it is Request.Match) {
                it.isoTemplates
            } else {
                emptyList()
            }
        }

        return ScannerViewModel(
            scanner,
            matcher,
            taskRunner,
            inputTemplates,
            request.fast
        ) as T
    }
}
