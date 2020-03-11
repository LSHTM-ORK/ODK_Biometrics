package uk.ac.lshtm.keppel.android.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import kotlinx.android.synthetic.main.activity_settings.*
import uk.ac.lshtm.keppel.android.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        open_source_licenses.setOnClickListener {
            OssLicensesMenuActivity.setActivityTitle(getString(R.string.open_source_libraries))
            startActivity(Intent(this, OssLicensesMenuActivity::class.java))
        }
    }
}