package uk.ac.lshtm.keppel.android.scanning

import android.content.Intent
import uk.ac.lshtm.keppel.android.OdkExternal
import uk.ac.lshtm.keppel.android.OdkExternalRequest

object IntentParser {
    fun parse(intent: Intent): Request {
        val odkExternalRequest = OdkExternal.parseIntent(intent)
        val fast = odkExternalRequest.params[OdkExternal.PARAM_FAST] == "true"

        return if (odkExternalRequest.action == OdkExternal.ACTION_MATCH) {
            Request.Match(
                intent.extras!!.getString(OdkExternal.PARAM_ISO_TEMPLATE),
                fast,
                odkExternalRequest
            )
        } else {
            Request.Scan(
                fast,
                odkExternalRequest
            )
        }
    }
}

sealed interface Request {

    val fast: Boolean
    val odkExternalRequest: OdkExternalRequest

    data class Scan(
        override val fast: Boolean,
        override val odkExternalRequest: OdkExternalRequest
    ) : Request

    data class Match(
        val isoTemplate: String?, override val fast: Boolean,
        override val odkExternalRequest: OdkExternalRequest
    ) : Request
}
