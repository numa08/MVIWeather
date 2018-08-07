package net.numa08.mviweather.presentation.result

import net.numa08.mviweather.data.City
import net.numa08.mviweather.mvibase.MviResult
import net.numa08.openweathermaplib.models.threehourforecast.ThreeHourForecast

sealed class CityDetailViewResult : MviResult {
    sealed class GetForecastResult : CityDetailViewResult() {
        data class Success(val forecast: ThreeHourForecast) : GetForecastResult()
        data class Failure(val error: Throwable) : GetForecastResult()
        data class InFlight(val city: City) : GetForecastResult()
    }
}