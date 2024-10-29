package uk.ac.lshtm.keppel.android.scanning

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import uk.ac.lshtm.keppel.android.databinding.ActivityScanBinding
import uk.ac.lshtm.keppel.android.matcher
import uk.ac.lshtm.keppel.android.scannerFactory
import uk.ac.lshtm.keppel.android.taskRunner

class ScanActivity : AppCompatActivity() {

    private val request: Request by lazy { IntentParser.parse(intent) }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return when (loadFragmentClass(classLoader, className)) {
                    ScanFragment::class.java -> ScanFragment(
                        request,
                        scannerFactory(),
                        matcher(),
                        taskRunner()
                    )

                    else -> super.instantiate(classLoader, className)
                }
            }
        }

        super.onCreate(savedInstanceState)

        val binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
