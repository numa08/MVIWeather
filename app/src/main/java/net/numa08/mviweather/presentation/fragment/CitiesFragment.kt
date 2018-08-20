package net.numa08.mviweather.presentation.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import dagger.android.support.DaggerFragment
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import net.numa08.mviweather.R
import net.numa08.mviweather.data.City
import net.numa08.mviweather.mvibase.MviView
import net.numa08.mviweather.presentation.NavigationController
import net.numa08.mviweather.presentation.NavigationEvent
import net.numa08.mviweather.presentation.adapter.CitiesAdapter
import net.numa08.mviweather.presentation.intent.CitiesViewIntent
import net.numa08.mviweather.presentation.state.CitiesViewState
import net.numa08.mviweather.presentation.viewmodel.CitiesViewModel
import javax.inject.Inject

class CitiesFragment : DaggerFragment(), MviView<CitiesViewIntent, CitiesViewState> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var navigationController: NavigationController

    private val viewModel: CitiesViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, viewModelFactory).get(CitiesViewModel::class.java)
    }

    private lateinit var adapter: CitiesAdapter

    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cities, container, false).also {
            adapter = CitiesAdapter(it.context)
            it.findViewById<ListView>(R.id.list).adapter = adapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun bind() {
        disposables.add(viewModel.states().subscribe(::render))
        viewModel.processIntents(intents())
        disposables.add(adapter.citySelectedObserver.subscribe { showCityWeatherDetail(it) })
    }

    override fun intents(): Observable<CitiesViewIntent> = Observable.just(CitiesViewIntent.InitialIntent)

    override fun render(state: CitiesViewState) {
        if (state.cityWeathers.isNotEmpty()) {
            adapter.clear()
            adapter.addAll(state.cityWeathers)
        }
    }

    private fun showCityWeatherDetail(city: City) {
        navigationController.navigateTo(NavigationEvent.WeatherDetailForCity(city))
    }
}