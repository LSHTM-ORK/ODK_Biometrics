package uk.ac.lshtm.keppel.android

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MessageDialogFragment : DialogFragment() {

    private val args: MessageDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setMessage(args.message)
            .setPositiveButton(R.string.ok) { _, _ ->
                findNavController().popBackStack() // Pop before sending result

                if (args.shouldFinish) {
                    parentFragmentManager.setFragmentResult(REQUEST_FATAL, Bundle())
                }
            }
            .show()
    }

    companion object {
        const val REQUEST_FATAL = "fatal"
    }
}
