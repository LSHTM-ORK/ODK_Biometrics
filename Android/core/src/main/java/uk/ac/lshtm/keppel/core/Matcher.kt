package uk.ac.lshtm.keppel.core

interface Matcher {

    fun match(one: ByteArray, two: ByteArray): Double?
}
