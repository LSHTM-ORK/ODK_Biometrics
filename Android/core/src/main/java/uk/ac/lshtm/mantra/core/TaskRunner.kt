package uk.ac.lshtm.mantra.core

interface TaskRunner {

    fun execute(runnable: () -> Unit)
}