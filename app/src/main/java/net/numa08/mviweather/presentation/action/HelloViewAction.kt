package net.numa08.mviweather.presentation.action

import net.numa08.mviweather.mvibase.MviAction

sealed class HelloViewAction : MviAction {
    /** 初期状態の State を表示するためのアクション */
    object ShowInitialMessage : HelloViewAction()

    /** クリック数のインクリメントを行うためのアクション */
    object IncrementCount : HelloViewAction()
}