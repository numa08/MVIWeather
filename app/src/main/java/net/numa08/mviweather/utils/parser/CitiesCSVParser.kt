package net.numa08.mviweather.utils.parser

import net.numa08.mviweather.data.City
import java.io.BufferedReader
import java.io.Reader

class CitiesCSVParser {

    companion object {
        const val NUMBER_OF_COLUMNS = 3
    }

    class InvalidCSVException(message: String): Exception(message)

    fun parse(from: Reader) : List<City> {
        val bufferedReader = BufferedReader(from)
        return bufferedReader.useLines { lines ->
            lines.map {
                val columns = it.split(",")
                if (columns.size != NUMBER_OF_COLUMNS) {
                    throw InvalidCSVException("csv line should have 3 column, line : $it")
                }
                val name = columns.first()
                val hepburn = columns[2]
                City(name, nameAsHepburn = hepburn)
            }.toList()
        }
    }

}