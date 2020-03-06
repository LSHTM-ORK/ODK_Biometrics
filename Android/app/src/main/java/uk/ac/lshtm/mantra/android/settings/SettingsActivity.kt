package uk.ac.lshtm.mantra.android.settings

import android.app.Activity
import android.os.Bundle
import uk.ac.lshtm.mantra.android.R

class SettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }
}