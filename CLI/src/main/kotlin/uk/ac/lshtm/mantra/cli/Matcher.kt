package uk.ac.lshtm.mantra.cli

interface Matcher {

    fun match(one: ByteArray, two: ByteArray): Double
}