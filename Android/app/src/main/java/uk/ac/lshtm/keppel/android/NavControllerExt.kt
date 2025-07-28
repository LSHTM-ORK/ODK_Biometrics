package uk.ac.lshtm.keppel.android

import androidx.navigation.NavController
import androidx.navigation.NavDirections

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
}
