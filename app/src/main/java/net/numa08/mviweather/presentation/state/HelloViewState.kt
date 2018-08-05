package net.numa08.mviweather.presentation.state

import net.numa08.mviweather.mvibase.MviViewState

data class HelloViewState(
        /** ボタンが押された回数 */
        val numberOfButtonClicked: Int?,
        /** 初期メッセージを表示するかどうか */
        val showInitialMessage: Boolean
) : MviViewState {

    companion object {
        /** 初期状態の State */
        fun initial(): HelloViewState =
                HelloViewState(numberOfButtonClicked = null, showInitialMessage = true)
    }

}