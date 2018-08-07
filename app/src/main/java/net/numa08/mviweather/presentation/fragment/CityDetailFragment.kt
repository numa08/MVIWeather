package net.numa08.mviweather.presentation.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import net.numa08.mviweather.R
import net.numa08.mviweather.data.City
import net.numa08.mviweather.mvibase.MviView
import net.numa08.mviweather.presentation.intent.CityDetailViewIntent
import net.numa08.mviweather.presentation.state.CityDetailViewState
import net.numa08.mviweather.presentation.viewmodel.CityDetailViewModel
import javax.inject.Inject

class CityDetailFragment : DaggerFragment(), MviView<CityDetailViewIntent, CityDetailViewState> {

    companion object {
        private object Args {
            const val CITY_NAME = "city_name"
            const val CITY_HEPBURN_NAME = "city_hepburn_name"
        }

        fun instance(city: City): CityDetailFragment = CityDetailFragment().also { fragment ->
            val args = Bundle().also {
                it.putString(Args.CITY_NAME, city.name)
                it.putString(Args.CITY_HEPBURN_NAME, city.nameAsHepburn)
            }
            fragment.arguments = args
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CityDetailViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, viewModelFactory).get(CityDetailViewModel::class.java)
    }
    private val disposable = CompositeDisposable()
    private val city: City
        get() {
            val arg = arguments ?: throw IllegalArgumentException("argument is null")
            val name = arg.getString(Args.CITY_NAME)
                    ?: throw IllegalArgumentException("city_name is null")
            val hepburnName = arg.getString(Args.CITY_HEPBURN_NAME)
                    ?: throw IllegalArgumentException("city_hepburn_name is null")
            return City(name, hepburnName)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_city_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    private fun bind() {
        disposable.add(viewModel.states().subscribe(::render))
        viewModel.processIntents(intents())
    }

    override fun intents(): Observable<CityDetailViewIntent> {
        return Observable.just(CityDetailViewIntent.InitialIntent(city))

    }

    override fun render(state: CityDetailViewState) {
        Log.d("test", "state is $state")
    }
}