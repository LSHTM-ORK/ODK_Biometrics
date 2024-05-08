package uk.ac.lshtm.keppel.android.scanning

import android.content.Intent
import uk.ac.lshtm.keppel.android.OdkExternal

object IntentParser {
    fun parse(intent: Intent): Request {
        val fast = intent.extras?.getString(OdkExternal.PARAM_FAST) == "true"

        return if (intent.action == OdkExternal.ACTION_MATCH) {
            Request.Match(
                intent.extras!!.getString(OdkExternal.PARAM_ISO_TEMPLATE),
                fast
            )
        } else {
            Request.Scan(
                fast
            )
        }
    }
}

sealed interface Request {
    val fast: Boolean

    data class Scan(override val fast: Boolean) : Request
    data class Match(val isoTemplate: String?, override val fast: Boolean) : Request
}
