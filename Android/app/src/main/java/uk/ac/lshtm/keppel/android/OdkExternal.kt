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

    fun isSingleReturn(intent: Intent): Boolean {
        return intent.extras?.containsKey(PARAM_INPUT_VALUE) == true
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

    fun buildMultipleReturnResult(inputIntent: Intent, results: Map<String, Any>): Intent {
        return Intent().also {
            results.forEach { (key, value) ->
                if (inputIntent.hasExtra(key)) {
                    val returnExtra = inputIntent.getStringExtra(key)

                    when (value) {
                        is Double -> it.putExtra(returnExtra, value)
                        is Int -> it.putExtra(returnExtra, value)
                        else -> it.putExtra(returnExtra, value as String)
                    }
                }
            }
        }
    }
}
