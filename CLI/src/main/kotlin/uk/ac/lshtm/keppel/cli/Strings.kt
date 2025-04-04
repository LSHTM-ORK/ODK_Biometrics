package uk.ac.lshtm.keppel.cli

object Strings {
    const val PLAIN_TEXT_HELP = "Treat TEMPLATE_ONE and TEMPLATE_TWO as plain text rather than file paths"
    const val MATCH_WITH_SCORE_HELP = "Return whether templates match along with score like \"match_210.124\""
    const val MATCH_WITHOUT_SCORE_HELP = "Return whether templates match (either \"match\" or \"mismatch\")"
    const val THRESHOLD_HELP = "Threshold (score) to be used to determine whether templates are a match or mismatch"

    const val INPUT_CSV_HELP = "Path to input CSV"
    const val OUTPUT_CSV_HELP = "Path to output CSV"
    const val PARALLELISM_HELP = "How many threads should be used"

    const val ERROR_PMATCH_NO_HEADER_ROW = "Input CSV does not have header row!"
}
