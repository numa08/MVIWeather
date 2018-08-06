package net.numa08.mviweather.presentation.state

import net.numa08.mviweather.data.City
import net.numa08.mviweather.mvibase.MviViewState

data class CityDetailViewState(
        val city: City
) : MviViewState