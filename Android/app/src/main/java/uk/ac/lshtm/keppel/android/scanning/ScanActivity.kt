package uk.ac.lshtm.keppel.android.scanning

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import uk.ac.lshtm.keppel.android.External
import uk.ac.lshtm.keppel.android.OdkExternal
import uk.ac.lshtm.keppel.android.OdkExternalRequest
import uk.ac.lshtm.keppel.android.databinding.ActivityScanBinding
import uk.ac.lshtm.keppel.android.matcher
import uk.ac.lshtm.keppel.android.scannerFactory
import uk.ac.lshtm.keppel.android.taskRunner
import uk.ac.lshtm.keppel.core.CaptureResult

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

        supportFragmentManager.setFragmentResultListener(ScanFragment.RESULT, this) { _, result ->
            if (result.containsKey(ScanFragment.RESULT_KEY_SCAN)) {
                val scanResult =
                    result.getSerializable(ScanFragment.RESULT_KEY_SCAN) as ScanFragment.ScanResult
                returnResult(buildScanReturn(request.odkExternalRequest, scanResult.captureResult))
            } else if (result.containsKey(ScanFragment.RESULT_KEY_MATCH)) {
                val matchResult =
                    result.getSerializable(ScanFragment.RESULT_KEY_MATCH) as ScanFragment.MatchResult
                returnResult(
                    buildMatchReturn(
                        request.odkExternalRequest,
                        matchResult.score,
                        matchResult.captureResult
                    )
                )
            } else {
                finish()
            }
        }

        super.onCreate(savedInstanceState)
        val binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun returnResult(result: Intent) {
        setResult(RESULT_OK, result)
        finish()
    }

    private fun buildScanReturn(
        odkExternalRequest: OdkExternalRequest,
        capture: CaptureResult
    ): Intent {
        return if (odkExternalRequest.isSingleReturn) {
            OdkExternal.buildSingleReturnIntent(capture.isoTemplate)
        } else {
            OdkExternal.buildMultipleReturnResult(
                odkExternalRequest.params, mapOf(
                    External.PARAM_RETURN_ISO_TEMPLATE to capture.isoTemplate,
                    External.PARAM_RETURN_NFIQ to capture.nfiq
                )
            )
        }
    }

    private fun buildMatchReturn(
        odkExternalRequest: OdkExternalRequest,
        score: Double,
        capture: CaptureResult
    ): Intent {
        return if (odkExternalRequest.isSingleReturn) {
            OdkExternal.buildSingleReturnIntent(score)
        } else {
            OdkExternal.buildMultipleReturnResult(
                odkExternalRequest.params, mapOf(
                    External.PARAM_RETURN_SCORE to score,
                    External.PARAM_RETURN_ISO_TEMPLATE to capture.isoTemplate,
                    External.PARAM_RETURN_NFIQ to capture.nfiq
                )
            )
        }
    }
}
