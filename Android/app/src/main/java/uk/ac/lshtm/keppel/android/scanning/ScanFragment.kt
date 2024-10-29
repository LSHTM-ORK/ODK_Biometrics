package uk.ac.lshtm.keppel.android.scanning

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import uk.ac.lshtm.keppel.android.External
import uk.ac.lshtm.keppel.android.FatalErrorDialogFragment
import uk.ac.lshtm.keppel.android.OdkExternal
import uk.ac.lshtm.keppel.android.OdkExternalRequest
import uk.ac.lshtm.keppel.android.R
import uk.ac.lshtm.keppel.android.databinding.FragmentScanBinding
import uk.ac.lshtm.keppel.android.scanning.ScannerViewModel.ScannerState
import uk.ac.lshtm.keppel.core.Analytics
import uk.ac.lshtm.keppel.core.CaptureResult
import uk.ac.lshtm.keppel.core.Matcher
import uk.ac.lshtm.keppel.core.Scanner
import uk.ac.lshtm.keppel.core.TaskRunner

class ScanFragment(
    private val request: Request,
    private val scannerFactory: ScannerFactory,
    private val matcher: Matcher,
    private val taskRunner: TaskRunner
) : Fragment() {

    private val viewModel: ScannerViewModel by viewModels {
        ScanViewModelFactory(
            scannerFactory.create(requireActivity()),
            matcher,
            taskRunner,
            request
        )
    }

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
                    FatalErrorDialogFragment.show(parentFragmentManager, error)

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
                    FatalErrorDialogFragment.show(
                        parentFragmentManager,
                        getString(R.string.connection_failure_error)
                    )
                }
            }
        }

        viewModel.result.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                processResult(request, result)
            }
        }

        binding.captureButton.setOnClickListener {
            viewModel.capture()
        }

        binding.cancelButton.setOnClickListener {
            Analytics.log("cancel")
            requireActivity().finish()
        }

        if (request is Request.Match) {
            binding.captureButton.setText(R.string.match)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopCapture()
    }

    private fun processResult(request: Request, result: ScannerViewModel.Result) {
        when (result) {
            is ScannerViewModel.Result.Match -> {
                Analytics.log("match_return")

                returnResult(
                    buildMatchReturn(
                        request.odkExternalRequest,
                        result.score,
                        result.captureResult
                    )
                )
            }

            is ScannerViewModel.Result.Scan -> {
                Analytics.log("scan_return")
                returnResult(buildScanReturn(request.odkExternalRequest, result.captureResult))
            }

            is ScannerViewModel.Result.InputError -> {
                FatalErrorDialogFragment.show(
                    parentFragmentManager,
                    getString(R.string.input_error)
                )
            }

            is ScannerViewModel.Result.NoCaptureResultError -> {
                FatalErrorDialogFragment.show(
                    parentFragmentManager,
                    getString(R.string.no_capture_result_error)
                )
            }
        }
    }

    private fun returnResult(result: Intent) {
        requireActivity().setResult(RESULT_OK, result)
        requireActivity().finish()
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
