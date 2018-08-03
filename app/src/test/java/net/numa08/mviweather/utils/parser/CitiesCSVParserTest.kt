@file:Suppress("RemoveRedundantBackticks")

package net.numa08.mviweather.utils.parser

import assertk.assert
import assertk.assertions.*
import org.junit.Test

class CitiesCSVParserTest {

    val csv = """
北海道,ほっかいどう,hokkaido
青森県,あおもり,aomori
岩手県,いわて,iwate
宮城県,みやぎ,miyagi
秋田県,あきた,akita
    """.trimIndent()

    val errorCsv = """
北海道,ほっかいどう,hokkaido
青森県,あおもり,aomori
岩手県,iwate
宮城県,みやぎ,miyagi
秋田県,あきた,akita
    """.trimIndent()


    @Test
    fun `CSVを正しくパースできること`() {
        csv.reader().use {
            val parser = CitiesCSVParser()
            val cities = parser.parse(it)
            assert(cities.size).isEqualTo(5)
        }
    }

    @Test(expected = CitiesCSVParser.InvalidCSVException::class)
    fun  `CSVのパースに失敗したら例外をスローすること`() {
        errorCsv.reader().use {
            val parser = CitiesCSVParser()
            parser.parse(it)
        }
    }



}