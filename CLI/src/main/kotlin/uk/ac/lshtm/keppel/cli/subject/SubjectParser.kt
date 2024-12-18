package uk.ac.lshtm.keppel.cli.subject

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.File

object SubjectParser {
    @Throws(BadHeaderException::class)
    fun parseCsv(file: String): List<Subject> {
        val csvParser = CSVParser(
            File(file).reader(),
            CSVFormat.Builder.create()
                .setDelimiter(',')
                .setTrim(true)
                .setHeader()
                .build()
        )

        if (csvParser.headerNames.isEmpty() || csvParser.headerNames[0] != "id") {
            throw BadHeaderException()
        }

        return csvParser.asSequence().map { row ->
            val templates = 1.until(row.size()).map {
                row.get("template_$it")
            }

            Subject(row.get("id"), templates)
        }.toList()
    }

    class BadHeaderException : Exception()
}