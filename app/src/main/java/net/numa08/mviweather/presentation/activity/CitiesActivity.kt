package net.numa08.mviweather.presentation.activity

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import net.numa08.mviweather.R
import net.numa08.mviweather.mvibase.MviView
import net.numa08.mviweather.presentation.intent.CitiesViewIntent
import net.numa08.mviweather.presentation.state.CitiesViewState
import net.numa08.mviweather.presentation.viewmodel.CitiesViewModel
import javax.inject.Inject

class CitiesActivity : DaggerAppCompatActivity(), MviView<CitiesViewIntent, CitiesViewState> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: CitiesViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, viewModelFactory).get(CitiesViewModel::class.java)
    }
    private val disposables = CompositeDisposable()

    override fun intents(): Observable<CitiesViewIntent>
        = Observable.just(CitiesViewIntent.InitialIntent)

    override fun render(state: CitiesViewState) {
        Log.d("test", "$state")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities)
        disposables.add(viewModel.states().subscribe(::render))
        viewModel.processIntents(intents())
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}
