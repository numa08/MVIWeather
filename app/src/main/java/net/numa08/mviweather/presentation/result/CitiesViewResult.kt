package net.numa08.mviweather.presentation.result

import net.numa08.mviweather.data.City
import net.numa08.mviweather.mvibase.MviResult

sealed class CitiesViewResult : MviResult {
    sealed class LoadCitiesResult: CitiesViewResult() {
        data class Success(val cities: List<City>): LoadCitiesResult()
        data class Failure(val error: Throwable): LoadCitiesResult()
        object InFlight: LoadCitiesResult()
    }
}