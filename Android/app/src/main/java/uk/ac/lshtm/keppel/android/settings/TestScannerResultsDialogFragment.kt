package uk.ac.lshtm.keppel.android.settings

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import uk.ac.lshtm.keppel.android.R
import uk.ac.lshtm.keppel.android.databinding.FragmentTestScannerResultsDialogBinding
import uk.ac.lshtm.keppel.core.takeMiddle

class TestScannerResultsDialogFragment : DialogFragment() {

    private val args: TestScannerResultsDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = FragmentTestScannerResultsDialogBinding.inflate(layoutInflater)

        val template = args.template
        if (template.length <= MAX_TEMPLATE_LENGTH) {
            binding.template.text = template
        } else {
            val shortenedTemplate = template.takeMiddle(MAX_TEMPLATE_LENGTH)
            binding.template.text = getString(R.string.shortened_template, shortenedTemplate)
        }

        binding.nfiq.text = args.nfiq.toString()

        return MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .setPositiveButton(R.string.ok, null)
            .show()
    }

    companion object {
        private const val MAX_TEMPLATE_LENGTH = 16
    }
}
