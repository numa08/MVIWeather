package net.numa08.mviweather.utils.parser

import net.numa08.mviweather.data.City
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class CitiesResponseBodyConverter(
        private val parser: CitiesCSVParser
): Converter<ResponseBody, List<City>> {

    override fun convert(value: ResponseBody?): List<City> {
        if (value == null) return emptyList()
        val body = value.string()
        return body.reader().use { parser.parse(it) }
    }

    class Factory(private val parser: CitiesCSVParser) : Converter.Factory() {

        companion object {
            fun create(parser: CitiesCSVParser) = Factory(parser)
        }

        override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>?
            = CitiesResponseBodyConverter(parser)
    }
}
