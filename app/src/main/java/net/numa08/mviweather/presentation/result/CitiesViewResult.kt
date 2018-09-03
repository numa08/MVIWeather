package net.numa08.mviweather.presentation.result

import net.numa08.mviweather.data.City
import net.numa08.mviweather.mvibase.MviResult
import net.numa08.openweathermaplib.models.common.Weather

sealed class CitiesViewResult : MviResult {
    data class CityWeather(val city: City, val weather: Weather?)
    sealed class LoadCitiesResult: CitiesViewResult() {
        data class Success(val cityWeather: CityWeather) : LoadCitiesResult()
        data class Failure(val error: Throwable): LoadCitiesResult()
        object InFlight: LoadCitiesResult()
    }
}