package net.numa08.mviweather.presentation.intent

import net.numa08.mviweather.mvibase.MviIntent

sealed class HelloViewIntent : MviIntent {
    /** ユーザが画面を開いたときに発行される Intent */
    object InitialIntent : HelloViewIntent()

    /** ユーザがカウント数を増やしたいときに発行される Intent */
    object IncrementCountIntent : HelloViewIntent()
}