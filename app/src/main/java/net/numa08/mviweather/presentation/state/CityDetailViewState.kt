package net.numa08.mviweather.presentation.state

import net.numa08.mviweather.data.City
import net.numa08.mviweather.mvibase.MviViewState
import net.numa08.openweathermaplib.models.threehourforecast.ThreeHourForecast

data class CityDetailViewState(
        val city: City? = null,
        val forecast: ThreeHourForecast? = null,
        val error: Throwable? = null,
        val isLoading: Boolean
) : MviViewState {
    companion object {
        fun idle(): CityDetailViewState = CityDetailViewState(isLoading = false)
    }
}