package net.numa08.mviweather.presentation.viewmodel

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import net.numa08.mviweather.data.source.CitiesDataSource
import net.numa08.mviweather.mvibase.MviViewModel
import net.numa08.mviweather.presentation.action.CitiesViewAction
import net.numa08.mviweather.presentation.intent.CitiesViewIntent
import net.numa08.mviweather.presentation.result.CitiesViewResult
import net.numa08.mviweather.presentation.state.CitiesViewState
import net.numa08.mviweather.utils.SchedulerProvider
import net.numa08.mviweather.utils.notOfType
import javax.inject.Inject

class CitiesViewModel @Inject constructor(
        private val citiesDataSource: CitiesDataSource,
        private val schedulerProvider: SchedulerProvider
): ViewModel(), MviViewModel<CitiesViewIntent, CitiesViewState> {

    private val intentSubject: PublishSubject<CitiesViewIntent> = PublishSubject.create()
    private val stateObserver: Observable<CitiesViewState> = compose()

    private val intentFilter: ObservableTransformer<CitiesViewIntent, CitiesViewIntent>
        get() = ObservableTransformer { intent ->
            intent.publish { shared ->
                Observable.merge(
                        shared.ofType(CitiesViewIntent.InitialIntent::class.java).take(1),
                        shared.notOfType(CitiesViewIntent.InitialIntent::class.java)
                )
            }
        }

    override fun processIntents(intents: Observable<CitiesViewIntent>) {
        intents.subscribe(intentSubject)
    }

    override fun states(): Observable<CitiesViewState> = stateObserver

    private val actionFromIntent
        get() = { intent: CitiesViewIntent ->
            when (intent) {
                CitiesViewIntent.InitialIntent -> CitiesViewAction.LoadCitiesAction
            }
        }

    private val loadCitiesProcessor = { _: CitiesViewAction.LoadCitiesAction ->
        citiesDataSource
                .getCities()
                .toObservable()
                .map { CitiesViewResult.LoadCitiesResult.Success(it) }
                .cast(CitiesViewResult.LoadCitiesResult::class.java)
                .onErrorReturn(CitiesViewResult.LoadCitiesResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(CitiesViewResult.LoadCitiesResult.InFlight)
    }

    private val processAction: ObservableTransformer<CitiesViewAction, CitiesViewResult>
        get() = ObservableTransformer { actions ->
            actions.flatMap { action ->
                when(action) {
                    is CitiesViewAction.LoadCitiesAction -> loadCitiesProcessor(action)
                }
            }
        }

    private fun compose(): Observable<CitiesViewState> {
        return intentSubject
                .compose(intentFilter)
                .map(actionFromIntent)
                .compose(processAction)
                .scan(CitiesViewState.idle(), reducer)
                .distinctUntilChanged()
                .replay(1)
                .autoConnect(0)
    }

    companion object {
        val reducer = BiFunction { previousState: CitiesViewState, result: CitiesViewResult ->
            when(result) {
                is CitiesViewResult.LoadCitiesResult -> when(result) {
                    is CitiesViewResult.LoadCitiesResult.InFlight -> previousState.copy(isLoading = true)
                    is CitiesViewResult.LoadCitiesResult.Success -> previousState.copy(isLoading = false, cities = result.cities)
                    is CitiesViewResult.LoadCitiesResult.Failure -> previousState.copy(isLoading = false, error = result.error)
                }
            }

        }
    }

}