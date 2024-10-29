package uk.ac.lshtm.keppel.android

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FatalErrorDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setMessage(requireArguments().getString(ARG_ERROR))
            .setPositiveButton(R.string.ok) { _, _ -> requireActivity().finish() }
            .show()
    }

    companion object {
        private const val ARG_ERROR = "error"

        fun show(fragmentManager: FragmentManager, error: String) {
            val fatalErrorDialogFragment = FatalErrorDialogFragment()
            fatalErrorDialogFragment.arguments = bundleOf(ARG_ERROR to error)

            val tag = FatalErrorDialogFragment::class.java.name
            fatalErrorDialogFragment.show(fragmentManager, tag)
        }
    }
}
