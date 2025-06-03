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
        binding.template.text = getString(R.string.shortened_template, args.template.takeMiddle(16))
        binding.nfiq.text = args.nfiq.toString()

        return MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .setPositiveButton(R.string.ok, null)
            .show()
    }
}


private fun String.takeMiddle(n: Int): String {
    val middle = this.length / 2
    val halfN = n / 2
    return this.substring(middle - halfN, middle + halfN)
}
