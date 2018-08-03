package net.numa08.mviweather.presentation.action

import net.numa08.mviweather.mvibase.MviAction

sealed class CitiesViewAction: MviAction {
    object LoadCitiesAction: CitiesViewAction()
}