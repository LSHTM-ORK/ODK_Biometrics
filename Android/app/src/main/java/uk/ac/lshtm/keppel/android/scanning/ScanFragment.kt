package uk.ac.lshtm.keppel.android.scanning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.fragment.findNavController
import uk.ac.lshtm.keppel.android.External
import uk.ac.lshtm.keppel.android.R
import uk.ac.lshtm.keppel.android.databinding.FragmentScanBinding
import uk.ac.lshtm.keppel.android.scanning.ScannerViewModel.ScannerState
import uk.ac.lshtm.keppel.core.Analytics
import uk.ac.lshtm.keppel.core.CaptureResult
import uk.ac.lshtm.keppel.core.Matcher
import uk.ac.lshtm.keppel.core.Scanner
import uk.ac.lshtm.keppel.core.TaskRunner
import java.io.Serializable

class ScanFragment(
    private val request: Request,
    private val scannerFactory: ScannerFactory,
    private val matcher: Matcher,
    private val taskRunner: TaskRunner,
    private val resultsFragmentManager: FragmentManager
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
            if (result != null) {
                processResult(result)
            }
        }

        binding.captureButton.setOnClickListener {
            viewModel.capture()
        }

        binding.cancelButton.setOnClickListener {
            Analytics.log("cancel")
            resultsFragmentManager.setFragmentResult(RESULT, Bundle())
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
        when (result) {
            is ScannerViewModel.Result.Match -> {
                Analytics.log("match_return")

                resultsFragmentManager.setFragmentResult(RESULT, Bundle().also {
                    it.putSerializable(
                        RESULT_KEY_MATCH,
                        MatchResult(result.captureResult, result.score)
                    )
                })
            }

            is ScannerViewModel.Result.Scan -> {
                Analytics.log("scan_return")
                resultsFragmentManager.setFragmentResult(RESULT, Bundle().also {
                    it.putSerializable(RESULT_KEY_SCAN, ScanResult(result.captureResult))
                })
            }

            is ScannerViewModel.Result.InputError -> {
                findNavController().navigate(ScanFragmentDirections.scanToFatalError(getString(R.string.input_error)))
            }

            is ScannerViewModel.Result.NoCaptureResultError -> {
                findNavController().navigate(ScanFragmentDirections.scanToFatalError(getString(R.string.no_capture_result_error)))
            }
        }
    }

    companion object {
        const val RESULT = "result"
        const val RESULT_KEY_SCAN = "result_key_scan"
        const val RESULT_KEY_MATCH = "result_key_match"
    }

    data class ScanResult(val captureResult: CaptureResult) : Serializable
    data class MatchResult(val captureResult: CaptureResult, val score: Double) : Serializable
}

private class ScanViewModelFactory(
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
