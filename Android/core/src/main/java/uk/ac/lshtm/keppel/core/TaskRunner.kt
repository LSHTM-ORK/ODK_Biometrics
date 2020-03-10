package uk.ac.lshtm.keppel.core

interface TaskRunner {

    fun execute(runnable: () -> Unit)
}