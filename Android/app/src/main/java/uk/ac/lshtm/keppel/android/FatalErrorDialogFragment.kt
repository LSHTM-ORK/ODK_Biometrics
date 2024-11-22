package uk.ac.lshtm.keppel.android

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FatalErrorDialogFragment : DialogFragment() {

    private val args: FatalErrorDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setMessage(args.error)
            .setPositiveButton(R.string.ok) { _, _ -> requireActivity().finish() }
            .show()
    }
}
