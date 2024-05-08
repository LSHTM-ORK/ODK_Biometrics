package uk.ac.lshtm.keppel.android

import android.content.Intent

object OdkExternal {
    const val PARAM_INPUT_VALUE = "value"
    const val PARAM_RETURN_VALUE = "value"

    private const val APP_ID = "uk.ac.lshtm.keppel.android"

    const val ACTION_SCAN = "$APP_ID.SCAN"
    const val ACTION_MATCH = "$APP_ID.MATCH"

    const val PARAM_RETURN_ISO_TEMPLATE = "$APP_ID.return_iso_template"
    const val PARAM_RETURN_NFIQ = "$APP_ID.return_nfiq"
    const val PARAM_RETURN_SCORE = "$APP_ID.return_score"
    const val PARAM_ISO_TEMPLATE = "$APP_ID.iso_template"

    const val PARAM_FAST = "$APP_ID.fast"

    fun isSingleReturn(odkExternalRequest: OdkExternalRequest): Boolean {
        return odkExternalRequest.inputValue != null
    }

    fun buildSingleReturnIntent(double: Double): Intent {
        return Intent().also {
            it.putExtra(PARAM_RETURN_VALUE, double)
        }
    }

    fun buildSingleReturnIntent(string: String): Intent {
        return Intent().also {
            it.putExtra(PARAM_RETURN_VALUE, string)
        }
    }

    fun buildMultipleReturnResult(
        params:  Map<String, String>,
        results: Map<String, Any>
    ): Intent {
        return Intent().also {
            results.forEach { (key, value) ->
                if (params.containsKey(key)) {
                    val returnExtra = params.get(key)

                    when (value) {
                        is Double -> it.putExtra(returnExtra, value)
                        is Int -> it.putExtra(returnExtra, value)
                        else -> it.putExtra(returnExtra, value as String)
                    }
                }
            }
        }
    }

    fun parseIntent(intent: Intent): OdkExternalRequest {
        val params = intent.extras?.keySet()?.associate { Pair(it, intent.getStringExtra(it)!!) }

        return OdkExternalRequest(
            intent.action!!,
            intent.getStringExtra(PARAM_INPUT_VALUE),
            params ?: emptyMap()
        )
    }
}

data class OdkExternalRequest(
    val action: String,
    val inputValue: String?,
    val params: Map<String, String>
)
