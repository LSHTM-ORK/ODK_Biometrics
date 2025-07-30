package uk.ac.lshtm.keppel.android

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

object NavControllerExt {

    /**
     * Navigate to the dialog at [dialogDestination], but if it's the current destination already
     * then call navigate "up" first. This prevents crashes where multiple calls due to
     * calls to [navigate] to a dialog happenign after the dialog is already showing - the dialog
     * most like won't have an action to navigate to itself.
     */
    fun NavController.navigateToDialog(dialogDestination: Int, directions: NavDirections) {
        if (currentDestination?.id == dialogDestination) {
            navigateUp()
        }

        navigate(directions)
    }

    /**
     * Pop back stack of [NavController] or finish the host [android.app.Activity] if the doing
     * so empties the backstack
     */
    fun Fragment.popBackStackOrFinish() {
        if (!findNavController().popBackStack()) {
            requireActivity().finish()
        }
    }
}
