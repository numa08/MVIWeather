package net.numa08.mviweather.presentation.intent

import net.numa08.mviweather.data.City
import net.numa08.mviweather.mvibase.MviIntent

sealed class CityDetailViewIntent : MviIntent {
    data class InitialIntent(val city: City) : CityDetailViewIntent()
}