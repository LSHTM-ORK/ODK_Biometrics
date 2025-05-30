package uk.ac.lshtm.keppel.android.scanning

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.navigation.fragment.NavHostFragment
import uk.ac.lshtm.keppel.android.databinding.ActivityScanBinding
import uk.ac.lshtm.keppel.android.dependencies

class ScanActivity : AppCompatActivity() {

    private val request: Request by lazy { IntentParser.parse(intent) }
    private val viewModelFactory by lazy {
        ScanViewModelFactory(
            this,
            request,
            dependencies()
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
            if (!result.getBoolean(ScanFragment.RESULT_CANCEL)) {
                returnResult(result.getParcelable(ScanFragment.RESULT_INTENT)!!)
            } else {
                finish()
            }
        }
    }

    private fun returnResult(result: Intent) {
        setResult(RESULT_OK, result)
        finish()
    }
}
