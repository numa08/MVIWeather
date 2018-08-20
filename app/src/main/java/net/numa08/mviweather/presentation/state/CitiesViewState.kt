package net.numa08.mviweather.presentation.state

import net.numa08.mviweather.data.City
import net.numa08.mviweather.mvibase.MviViewState
import net.numa08.openweathermaplib.models.common.Weather

data class CitiesViewState(
        val isLoading: Boolean,
        val cityWeathers: List<Pair<City, Weather?>>,
        var error: Throwable? = null
): MviViewState {

    companion object {
        fun idle(): CitiesViewState = CitiesViewState(true, emptyList())
    }

}