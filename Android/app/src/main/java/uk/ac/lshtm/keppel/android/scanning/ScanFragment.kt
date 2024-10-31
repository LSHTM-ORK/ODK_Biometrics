package uk.ac.lshtm.keppel.android.scanning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import uk.ac.lshtm.keppel.android.External
import uk.ac.lshtm.keppel.android.R
import uk.ac.lshtm.keppel.android.databinding.FragmentScanBinding
import uk.ac.lshtm.keppel.android.scanning.ScannerViewModel.ScannerState
import uk.ac.lshtm.keppel.core.Analytics

class ScanFragment(private val request: Request) : Fragment() {

    private val viewModel: ScannerViewModel by activityViewModels()

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
            if (result is ScannerViewModel.Result.InputError) {
                findNavController().navigate(
                    ScanFragmentDirections.scanToFatalError(getString(R.string.input_error))
                )
            } else if (result is ScannerViewModel.Result.NoCaptureResultError) {
                findNavController().navigate(
                    ScanFragmentDirections.scanToFatalError(getString(R.string.no_capture_result_error))
                )
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
}
