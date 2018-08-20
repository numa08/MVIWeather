package net.numa08.mviweather.presentation.viewmodel

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import net.numa08.mviweather.data.source.WeatherDataSource
import net.numa08.mviweather.mvibase.MviViewModel
import net.numa08.mviweather.presentation.action.CityDetailViewAction
import net.numa08.mviweather.presentation.intent.CityDetailViewIntent
import net.numa08.mviweather.presentation.result.CityDetailViewResult
import net.numa08.mviweather.presentation.state.CityDetailViewState
import net.numa08.mviweather.utils.SchedulerProvider
import net.numa08.mviweather.utils.notOfType
import javax.inject.Inject

class CityDetailViewModel @Inject constructor(
        private val weatherDataSource: WeatherDataSource,
        private val schedulerProvider: SchedulerProvider
) : ViewModel(), MviViewModel<CityDetailViewIntent, CityDetailViewState> {

    private val intentSubject: PublishSubject<CityDetailViewIntent> = PublishSubject.create()
    private val stateObserver: Observable<CityDetailViewState> = compose()

    private val intentFilter: ObservableTransformer<CityDetailViewIntent, CityDetailViewIntent>
        get() = ObservableTransformer { intent ->
            intent.publish { shared ->
                Observable.merge(
                        shared.ofType(CityDetailViewIntent.InitialIntent::class.java).take(1),
                        shared.notOfType(CityDetailViewIntent.InitialIntent::class.java)
                )
            }
        }

    private val actionFromIntent
        get() = { intent: CityDetailViewIntent ->
            when (intent) {
                is CityDetailViewIntent.InitialIntent -> CityDetailViewAction.GetForecastAction(intent.city)
            }
        }

    private val getForecastProcessor = { action: CityDetailViewAction.GetForecastAction ->
        weatherDataSource
                .getThreeHourForecastByCityName(action.city.nameAsHepburn)
                .toObservable()
                .map { CityDetailViewResult.GetForecastResult.Success(it) }
                .cast(CityDetailViewResult.GetForecastResult::class.java)
                .onErrorReturn(CityDetailViewResult.GetForecastResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(CityDetailViewResult.GetForecastResult.InFlight(action.city))
    }

    private val processAction: ObservableTransformer<CityDetailViewAction, CityDetailViewResult>
        get() = ObservableTransformer { actions ->
            actions.flatMap { action ->
                when (action) {
                    is CityDetailViewAction.GetForecastAction -> getForecastProcessor(action)
                }
            }
        }

    private fun compose(): Observable<CityDetailViewState> {
        return intentSubject
                .compose(intentFilter)
                .map(actionFromIntent)
                .compose(processAction)
                .scan(CityDetailViewState.idle(), reducer)
                .distinctUntilChanged()
                .replay(1)
                .autoConnect(0)
    }

    companion object {
        val reducer = BiFunction { previousState: CityDetailViewState, result: CityDetailViewResult ->
            when (result) {
                is CityDetailViewResult.GetForecastResult -> when (result) {
                    is CityDetailViewResult.GetForecastResult.InFlight -> previousState.copy(isLoading = true, city = result.city)
                    is CityDetailViewResult.GetForecastResult.Failure -> previousState.copy(isLoading = false, error = result.error)
                    is CityDetailViewResult.GetForecastResult.Success -> previousState.copy(isLoading = false, error = null, forecast = result.forecast)
                }
            }
        }
    }

    override fun processIntents(intents: Observable<CityDetailViewIntent>) {
        intents.subscribe(intentSubject)
    }

    override fun states(): Observable<CityDetailViewState> = stateObserver
}