package net.numa08.mviweather.presentation.activity

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Observable
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

    override fun intents(): Observable<CitiesViewIntent>
        = Observable.just(CitiesViewIntent.InitialIntent)

    override fun render(state: CitiesViewState) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities)
        Log.d("test","hoge $viewModel")
    }
}
