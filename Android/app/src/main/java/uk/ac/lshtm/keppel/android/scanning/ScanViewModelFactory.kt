package uk.ac.lshtm.keppel.android.scanning

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import uk.ac.lshtm.keppel.android.Dependencies
import uk.ac.lshtm.keppel.android.settings.Preferences

class ScanViewModelFactory(
    private val context: Context,
    private val request: Request,
    private val dependencies: Dependencies,
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

        val scanners = dependencies.scanners
        val preferences = Preferences.get(context, scanners)
        val scannerFactory = scanners.first {
            it.name == preferences.getString(Preferences.SCANNER, null)
        }.create(context)

        return ScannerViewModel(
            scannerFactory,
            dependencies.matcher,
            dependencies.taskRunner,
            inputTemplates,
            request.fast
        ) as T
    }
}
