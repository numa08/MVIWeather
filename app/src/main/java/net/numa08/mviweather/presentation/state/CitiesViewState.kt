package net.numa08.mviweather.presentation.state

import net.numa08.mviweather.data.City
import net.numa08.mviweather.mvibase.MviViewState

data class CitiesViewState(
        val isLoading: Boolean,
        val cities: List<City>
): MviViewState {

    companion object {
        fun idle(): CitiesViewState = CitiesViewState(true, emptyList())
    }

}