package net.numa08.mviweather.presentation.action

import net.numa08.mviweather.data.City
import net.numa08.mviweather.mvibase.MviAction

sealed class CityDetailViewAction : MviAction {
    data class GetForecastAction(val city: City) : CityDetailViewAction()
}