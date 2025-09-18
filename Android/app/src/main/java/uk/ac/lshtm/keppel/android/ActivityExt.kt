package uk.ac.lshtm.keppel.android

import android.app.Activity
import android.content.res.Configuration
import androidx.core.view.WindowCompat

object ActivityExt {

    fun Activity.enableEdgeToEdge() {
        WindowCompat.enableEdgeToEdge(window)
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
            currentNightMode == Configuration.UI_MODE_NIGHT_NO

    }
}
