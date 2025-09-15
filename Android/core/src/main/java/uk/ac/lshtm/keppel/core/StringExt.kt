package uk.ac.lshtm.keppel.core

fun String.takeMiddle(n: Int): String {
    val middle = this.length / 2
    val halfN = n / 2
    return this.substring(middle - halfN, middle + halfN)
}
