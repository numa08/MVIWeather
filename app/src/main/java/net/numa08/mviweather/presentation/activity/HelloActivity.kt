package net.numa08.mviweather.presentation.activity

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import net.numa08.mviweather.R
import net.numa08.mviweather.mvibase.MviView
import net.numa08.mviweather.presentation.intent.HelloViewIntent
import net.numa08.mviweather.presentation.state.HelloViewState
import net.numa08.mviweather.presentation.viewmodel.HelloViewModel

class HelloActivity : AppCompatActivity(), MviView<HelloViewIntent, HelloViewState> {

    private val viewModel: HelloViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this).get(HelloViewModel::class.java)
    }
    private val disposables = CompositeDisposable()

    /** クリック数インクリメントを行う Intent を通知する */
    private val incrementCountIntentPublisher =
            PublishSubject.create<HelloViewIntent.IncrementCountIntent>()
    private lateinit var messageText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)
        messageText = findViewById(R.id.text)
        findViewById<Button>(R.id.button).setOnClickListener {
            // ボタンがクリックされたら、クリック数インクリメントを ViewModel に通知する
            incrementCountIntentPublisher.onNext(HelloViewIntent.IncrementCountIntent)
        }

        bind()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun bind() {
        // viewModel の処理結果を受け取って render で描画を行う
        disposables.add(viewModel.states().subscribe(::render))
        viewModel.processIntents(intents())
    }

    private fun initialIntent(): Observable<HelloViewIntent.InitialIntent> = Observable.just(HelloViewIntent.InitialIntent)

    private fun incrementCountIntent(): Observable<HelloViewIntent.IncrementCountIntent> = incrementCountIntentPublisher

    /**
     * ViewModel が処理をする Intent の一覧を返す。
     * Merge を使うことで、Intent が発生した順番で通知される*/
    override fun intents(): Observable<HelloViewIntent> = Observable.merge(
            initialIntent(),
            incrementCountIntent()
    )

    /**
     * ViewModel が Intent を処理した結果を使って描画をする。
     * */
    override fun render(state: HelloViewState) {
        if (state.showInitialMessage) {
            messageText.setText(R.string.hello)
            return
        }
        state.numberOfButtonClicked?.let {
            messageText.text = getString(R.string.number_of_count, it)
        }
    }

}
