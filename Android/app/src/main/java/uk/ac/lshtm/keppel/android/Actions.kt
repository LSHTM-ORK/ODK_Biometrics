package uk.ac.lshtm.keppel.android

object Actions {
    const val EXTRA_ODK_INPUT_VALUE = "value"
    const val EXTRA_ODK_RETURN_VALUE = "value"

    object Scan {
        const val ID = "uk.ac.lshtm.keppel.android.SCAN"

        const val EXTRA_ISO_TEMPLATE = "iso_template"
        const val EXTRA_NFIQ = "nfiq"
    }

    object Match {
        const val ID = "uk.ac.lshtm.keppel.android.MATCH"

        const val EXTRA_ISO_TEMPLATE = "iso_template"
    }
}
