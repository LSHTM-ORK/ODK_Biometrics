package uk.ac.lshtm.keppel.android

import android.content.Intent

object OdkExternal {

    const val PARAM_INPUT_VALUE = "value"
    const val PARAM_RETURN_VALUE = "value"

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
        params: Map<String, String>,
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
        val params = intent.extras?.keySet()?.fold(emptyMap<String, String>()) { map, key ->
            val value = intent.getStringExtra(key)
            if (value != null) {
                map + mapOf(key to value)
            } else {
                map
            }
        }

        return OdkExternalRequest(
            intent.action!!,
            intent.extras?.containsKey(PARAM_INPUT_VALUE) ?: false,
            intent.getStringExtra(PARAM_INPUT_VALUE),
            params ?: emptyMap()
        )
    }
}

data class OdkExternalRequest(
    val action: String,
    val isSingleReturn: Boolean,
    val inputValue: String?,
    val params: Map<String, String>
)
