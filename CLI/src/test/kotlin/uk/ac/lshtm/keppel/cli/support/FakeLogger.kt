package uk.ac.lshtm.keppel.cli.support

import uk.ac.lshtm.keppel.cli.Logger

class FakeLogger : Logger {
    private val _lines: ArrayList<String> = arrayListOf()

    val lines: List<String>
        get() = _lines

    override fun log(text: String) {
        _lines.add(text)
    }
}