package net.numa08.mviweather.presentation.result

import net.numa08.mviweather.mvibase.MviResult

sealed class HelloViewResult : MviResult {
    /** 初期状態の表示を行う */
    object ShowInitialMessageResult : HelloViewResult()

    /** ボタンが押された回数を返す */
    data class IncrementCountResult(val numberOfIncrement: Int) : HelloViewResult()
}