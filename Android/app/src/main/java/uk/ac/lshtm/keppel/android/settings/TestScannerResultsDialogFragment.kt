package uk.ac.lshtm.keppel.android.settings

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import uk.ac.lshtm.keppel.android.R
import uk.ac.lshtm.keppel.android.databinding.FragmentTestScannerResultsDialogBinding

class TestScannerResultsDialogFragment : DialogFragment() {

    private val args: TestScannerResultsDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = FragmentTestScannerResultsDialogBinding.inflate(layoutInflater)
        binding.template.text = args.template
        binding.nfiq.text = args.nfiq.toString()

        return MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .setPositiveButton(R.string.ok, null)
            .show()
    }
}
