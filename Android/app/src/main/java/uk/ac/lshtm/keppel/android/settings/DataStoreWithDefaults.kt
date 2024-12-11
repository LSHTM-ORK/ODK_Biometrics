package uk.ac.lshtm.keppel.android.settings

import android.content.SharedPreferences
import androidx.preference.PreferenceDataStore

class DataStoreWithDefaults(
    private val sharedPreferences: SharedPreferences,
    private val defaults: Map<String?, () -> String>
) : PreferenceDataStore() {
    override fun getString(key: String?, defValue: String?): String? {
        return if (sharedPreferences.contains(key)) {
            return sharedPreferences.getString(key, defValue)
        } else {
            defaults[key]?.invoke() ?: defValue
        }
    }

    override fun putString(key: String?, value: String?) {
       sharedPreferences.edit()
           .putString(key, value)
           .apply()
    }
}
