@file:Suppress("RemoveRedundantBackticks")

package net.numa08.mviweather.presentation.viewmodel

import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import net.numa08.mviweather.presentation.intent.HelloViewIntent
import net.numa08.mviweather.presentation.state.HelloViewState
import org.junit.Before
import org.junit.Test

class HelloViewModelTest {

    private lateinit var viewModel: HelloViewModel

    @Before
    fun initViewModel() {
        viewModel = HelloViewModel()
    }

    @Test
    fun `初期状態のステータスをテスト`() {
        // 期待される初期状態
        val expected = HelloViewState(numberOfButtonClicked = null, showInitialMessage = true)

        val subscriber = viewModel.states().test()
        // InitialIntent を発行する
        viewModel.processIntents(Observable.just(HelloViewIntent.InitialIntent))
        // Intent を処理させる
        subscriber.assertValue(expected)
    }

    @Test
    fun `ボタンを押した回数がインクリメントされること`() {
        // ボタンを3回クリックされた状態
        val subscriber = viewModel.states().test()
        // 初期状態→ボタンクリック*3 を実行する
        viewModel.processIntents(
                Observable.fromArray(
                        HelloViewIntent.InitialIntent,
                        HelloViewIntent.IncrementCountIntent,
                        HelloViewIntent.IncrementCountIntent,
                        HelloViewIntent.IncrementCountIntent
                )
        )
        subscriber.assertValues(
                HelloViewState.initial(),
                HelloViewState(numberOfButtonClicked = 1, showInitialMessage = false),
                HelloViewState(numberOfButtonClicked = 2, showInitialMessage = false),
                HelloViewState(numberOfButtonClicked = 3, showInitialMessage = false)
        )
    }

}