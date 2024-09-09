package uk.ac.lshtm.keppel.android.scanning

import android.content.Intent
import uk.ac.lshtm.keppel.android.External
import uk.ac.lshtm.keppel.android.OdkExternal
import uk.ac.lshtm.keppel.android.OdkExternalRequest

object IntentParser {
    fun parse(intent: Intent): Request {
        val odkExternalRequest = OdkExternal.parseIntent(intent)
        val fast = odkExternalRequest.params[External.PARAM_FAST] == "true"

        return if (odkExternalRequest.action == External.ACTION_MATCH) {
            val isoTemplate = odkExternalRequest.params[External.PARAM_ISO_TEMPLATE]

            Request.Match(
                isoTemplate?.let { listOf(isoTemplate) } ?: emptyList(),
                fast,
                odkExternalRequest
            )
        } else if (odkExternalRequest.action == External.ACTION_MULTI_MATCH) {
            var index = 1
            val isoTemplates = mutableListOf<String>()
            while (odkExternalRequest.params.containsKey(External.paramIsoTemplate(index))) {
                val paramValue = odkExternalRequest.params[External.paramIsoTemplate(index)]
                if (paramValue!!.isNotBlank()) {
                    isoTemplates.add(paramValue)
                }

                index += 1
            }

            Request.Match(
                isoTemplates,
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
        val isoTemplates: List<String>, override val fast: Boolean,
        override val odkExternalRequest: OdkExternalRequest
    ) : Request
}
