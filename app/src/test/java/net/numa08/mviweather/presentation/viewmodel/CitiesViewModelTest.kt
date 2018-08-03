@file:Suppress("RemoveRedundantBackticks")

package net.numa08.mviweather.presentation.viewmodel

import assertk.assert
import assertk.assertions.isEqualTo
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import net.numa08.mviweather.data.City
import net.numa08.mviweather.presentation.intent.CitiesViewIntent
import net.numa08.mviweather.presentation.result.CitiesViewResult
import net.numa08.mviweather.presentation.state.CitiesViewState
import org.junit.Before
import org.junit.Test

class CitiesViewModelTest {

    lateinit var viewMode: CitiesViewModel

    @Before
    fun initViewModel() {
        viewMode = CitiesViewModel()
    }

    @Test
    fun `初期状態の state をテスト`() {
        val expected = CitiesViewState(isLoading = true, cities = emptyList())

        val scheduler = TestScheduler()
        val subscriber = viewMode.states().subscribeOn(scheduler).test()
        viewMode.processIntents(Observable.just(CitiesViewIntent.InitialIntent))
        scheduler.triggerActions()
        subscriber.assertValue(expected)
    }

    @Test
    fun `読み込み中のreducer`() {
        val result = CitiesViewResult.LoadCitiesResult.InFlight
        val actual = CitiesViewModel.reducer.apply(CitiesViewState.idle() ,result).isLoading
        assert(actual).isEqualTo(true)
    }

    @Test
    fun `読み込み成功のreducer`() {
        val result = CitiesViewResult.LoadCitiesResult.Success(listOf(City("東京", "tokyo")))
        val actual = CitiesViewModel.reducer.apply(CitiesViewState.idle(), result)
        val expected = CitiesViewState(false, listOf(City("東京", "tokyo")))
        assert(actual).isEqualTo(expected)
    }

    @Test
    fun `読み込み失敗のreducer`() {
        val result = CitiesViewResult.LoadCitiesResult.Failure(Exception(""))
        val actual = CitiesViewModel.reducer.apply(CitiesViewState.idle(), result).isLoading
        assert(actual).isEqualTo(false)
    }

}