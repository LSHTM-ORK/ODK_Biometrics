package uk.ac.lshtm.keppel.cli

interface Matcher {

    fun match(one: ByteArray, two: ByteArray): Double
}