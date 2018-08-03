package net.numa08.mviweather.presentation.intent

import net.numa08.mviweather.mvibase.MviIntent

sealed class CitiesViewIntent : MviIntent{
    object InitialIntent: CitiesViewIntent()
}