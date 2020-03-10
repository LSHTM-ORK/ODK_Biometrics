package uk.ac.lshtm.keppel.android.settings

import android.app.Activity
import android.os.Bundle
import uk.ac.lshtm.keppel.android.R

class SettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }
}