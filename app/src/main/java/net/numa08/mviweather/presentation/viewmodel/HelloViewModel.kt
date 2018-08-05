package net.numa08.mviweather.presentation.viewmodel

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import net.numa08.mviweather.mvibase.MviViewModel
import net.numa08.mviweather.presentation.action.HelloViewAction
import net.numa08.mviweather.presentation.intent.HelloViewIntent
import net.numa08.mviweather.presentation.result.HelloViewResult
import net.numa08.mviweather.presentation.state.HelloViewState
import net.numa08.mviweather.utils.notOfType

class HelloViewModel : ViewModel(), MviViewModel<HelloViewIntent, HelloViewState> {

    /** 複数発行された InitialIntent を抑制する */
    private val intentFilter: ObservableTransformer<HelloViewIntent, HelloViewIntent>
        get() = ObservableTransformer { intent ->
            intent.publish { shared ->
                Observable.merge(
                        // InitialIntent に関しては最初の1回だけを処理する
                        shared.ofType(HelloViewIntent.InitialIntent::class.java).take(1),
                        // それ以外はとくにフィルターしない
                        shared.notOfType(HelloViewIntent.InitialIntent::class.java)
                )
            }
        }

    /** Intent -> Action に変換を行う関数 */
    private val actionFromIntent
        get() = { intent: HelloViewIntent ->
            when (intent) {
                HelloViewIntent.InitialIntent -> HelloViewAction.ShowInitialMessage
                HelloViewIntent.IncrementCountIntent -> HelloViewAction.IncrementCount
            }
        }

    /** Action を処理して Result を返す */
    private val processAction: ObservableTransformer<HelloViewAction, HelloViewResult>
        get() = ObservableTransformer { actions ->
            actions.flatMap { action ->
                when (action) {
                    // 初期メッセージを表示するだけ
                    HelloViewAction.ShowInitialMessage ->
                        Observable.just(HelloViewResult.ShowInitialMessageResult)
                    // ボタンを押されたら 1 をカウントアップする
                    HelloViewAction.IncrementCount ->
                        Observable.just(1).map(HelloViewResult::IncrementCountResult)
                }
            }
        }

    companion object {
        /**
         * MviView が描画を行うための State を返すための Reducer。
         * previousState には1つ前の Result から生成された State が入ってくる。 result に与えられた
         * 処理結果を利用して新しい State を作って返すことで、 MviView は描画を更新する。
         * */
        val reducer = BiFunction { previousState: HelloViewState, result: HelloViewResult ->
            when (result) {
                // 初期メッセージの表示は特に画面の更新は発生しないので、 previousState をそのまま返す
                HelloViewResult.ShowInitialMessageResult -> previousState
                // インクリメントが行われたら、previousState が管理しているクリック数に Result で得られた
                // インクリメントをする数を足して新しい State を作る
                is HelloViewResult.IncrementCountResult -> {
                    val previousCount = previousState.numberOfButtonClicked ?: 0
                    previousState.copy(
                            numberOfButtonClicked = previousCount + result.numberOfIncrement,
                            showInitialMessage = false
                    )
                }
            }
        }
    }

    /** Intent を State に変換する */
    private fun compose(): Observable<HelloViewState> = intentSubject
            // 複数発酵されてしまった InitialIntent を無視する
            .compose(intentFilter)
            // Intent を Action に変換する
            .map(actionFromIntent)
            // Action を処理する
            .compose(processAction)
            // scan を使うことで前の状態の State を利用して新しい State を作ることができる。
            // reduce を使ってしまうと complete が呼ばれるまで reduce の中身は実行されないため、
            // 適さない。
            .scan(HelloViewState.initial(), reducer)
            // State の状態が変わっていない場合には通知を出さない。
            .distinctUntilChanged()
            // 最後に発生したイベントを subscription に通知する。
            // 画面が回転して再度 MviView で subscribe したときに、1つ前の State が通知されるため、
            // 回転前の画面を維持できる。
            .replay(1)
            // subscribe されていなくても通知を発行して処理する。
            .autoConnect(0)

    /** 発酵された Intent を処理するための Observable.
     * intentSubject の後のオペレータが Intent を変換して処理していく */
    private val intentSubject: PublishSubject<HelloViewIntent> = PublishSubject.create()

    override fun processIntents(intents: Observable<HelloViewIntent>) {
        intents.subscribe(intentSubject)
    }

    /** 処理された Intent は State となって MviView に通知される */
    override fun states(): Observable<HelloViewState> = compose()

}