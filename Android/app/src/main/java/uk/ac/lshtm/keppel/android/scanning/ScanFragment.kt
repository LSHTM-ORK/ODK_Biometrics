package uk.ac.lshtm.keppel.android.scanning

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import uk.ac.lshtm.keppel.android.External
import uk.ac.lshtm.keppel.android.OdkExternal
import uk.ac.lshtm.keppel.android.OdkExternalRequest
import uk.ac.lshtm.keppel.android.R
import uk.ac.lshtm.keppel.android.databinding.FragmentScanBinding
import uk.ac.lshtm.keppel.android.scanning.ScannerViewModel.ScannerState
import uk.ac.lshtm.keppel.core.Analytics
import uk.ac.lshtm.keppel.core.CaptureResult

class ScanFragment(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val request: Request
) : Fragment() {

    private val viewModel: ScannerViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentScanBinding.inflate(layoutInflater).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentScanBinding.bind(view)

        when (request) {
            is Request.Match -> {
                Analytics.log("match_start")

                if (request.isoTemplates.isEmpty()) {
                    val error = getString(R.string.input_missing_error, External.PARAM_ISO_TEMPLATE)
                    findNavController().navigate(ScanFragmentDirections.scanToFatalError(error))

                    return
                }
            }

            is Request.Scan -> {
                Analytics.log("scan_start")
            }
        }

        viewModel.scannerState.observe(viewLifecycleOwner) { state ->
            when (state) {
                ScannerState.Disconnected -> {
                    binding.connectProgressBar.visibility = View.VISIBLE
                    binding.captureButton.visibility = View.GONE
                    binding.captureProgressBar.visibility = View.GONE
                }

                ScannerState.Connected -> {
                    binding.connectProgressBar.visibility = View.GONE
                    binding.captureButton.visibility = View.VISIBLE
                    binding.captureProgressBar.visibility = View.GONE
                }

                ScannerState.Scanning -> {
                    binding.connectProgressBar.visibility = View.GONE
                    binding.captureButton.visibility = View.GONE
                    binding.captureProgressBar.visibility = View.VISIBLE
                }

                ScannerState.ConnectionFailure -> {
                    findNavController().navigate(ScanFragmentDirections.scanToFatalError(getString(R.string.connection_failure_error)))
                }
            }
        }

        viewModel.result.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ScannerViewModel.Result.InputError -> {
                    findNavController().navigate(
                        ScanFragmentDirections.scanToFatalError(getString(R.string.input_error))
                    )
                }

                is ScannerViewModel.Result.NoCaptureResultError -> {
                    findNavController().navigate(
                        ScanFragmentDirections.scanToFatalError(getString(R.string.no_capture_result_error))
                    )
                }

                is ScannerViewModel.Result.Match -> processResult(result)
                is ScannerViewModel.Result.Scan -> processResult(result)
                null -> {}
            }
        }

        binding.captureButton.setOnClickListener {
            viewModel.capture()
        }

        binding.cancelButton.setOnClickListener {
            Analytics.log("cancel")
            parentFragmentManager.setFragmentResult(
                REQUEST_SCAN,
                Bundle().also { it.putBoolean(RESULT_CANCEL, true) }
            )

            findNavController().popBackStack()
        }

        if (request is Request.Match) {
            binding.captureButton.setText(R.string.match)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopCapture()
    }

    private fun processResult(result: ScannerViewModel.Result) {
        if (result is ScannerViewModel.Result.Match) {
            Analytics.log("match_return")

            returnResult(
                buildMatchReturn(
                    request.odkExternalRequest,
                    result.score,
                    result.captureResult
                )
            )
        } else if (result is ScannerViewModel.Result.Scan) {
            Analytics.log("scan_return")
            returnResult(buildScanReturn(request.odkExternalRequest, result.captureResult))
        }
    }

    private fun returnResult(intent: Intent) {
        parentFragmentManager.setFragmentResult(
            REQUEST_SCAN,
            Bundle().also { it.putParcelable(RESULT_INTENT, intent) }
        )

        findNavController().popBackStack()
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

    companion object {
        val REQUEST_SCAN = "scan"
        val RESULT_INTENT = "intent"
        val RESULT_CANCEL = "cancel"
    }
}
