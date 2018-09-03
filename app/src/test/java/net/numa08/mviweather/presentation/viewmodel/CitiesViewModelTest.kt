@file:Suppress("RemoveRedundantBackticks")

package net.numa08.mviweather.presentation.viewmodel

import assertk.assert
import assertk.assertions.isEqualTo
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.Single
import net.numa08.mviweather.data.City
import net.numa08.mviweather.data.source.CitiesDataSource
import net.numa08.mviweather.data.source.WeatherDataSource
import net.numa08.mviweather.presentation.intent.CitiesViewIntent
import net.numa08.mviweather.presentation.result.CitiesViewResult
import net.numa08.mviweather.presentation.state.CitiesViewState
import net.numa08.mviweather.utils.TestSchedulerProvider
import net.numa08.openweathermaplib.models.common.Weather
import net.numa08.openweathermaplib.models.currentweather.CurrentWeather
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class CitiesViewModelTest {


    @Mock
    private val citiesDataSource: CitiesDataSource = mock()
    @Mock
    private val weatherDataSource: WeatherDataSource = mock()

    private lateinit var viewModel: CitiesViewModel

    @Before
    fun initViewModel() {
        viewModel = CitiesViewModel(citiesDataSource, weatherDataSource, TestSchedulerProvider())
    }

    @Test
    fun `読み込み中にエラーが発生した場合`() {
        val error = RuntimeException("error")
        val response = Single.error<List<City>>(error)
        whenever(citiesDataSource.getCities()).thenReturn(response)

        val subscriber = viewModel.states().test()
        viewModel.processIntents(Observable.just(CitiesViewIntent.InitialIntent))

        subscriber.assertValues(
                CitiesViewState(isLoading = true, cityWeathers = emptyList(), error = null),
                CitiesViewState(isLoading = false, cityWeathers = emptyList(), error = error)
        )
    }

    @Test
    fun `天気の読み込みに成功した場合`() {
        val city = City("東京", "tokyo")
        val currentWeather: CurrentWeather = mock()
        val weather: Weather = mock()
        whenever(currentWeather.weatherArray).thenReturn(arrayListOf(weather))

        whenever(citiesDataSource.getCities()).thenReturn(Single.just(listOf(city)))
        whenever(weatherDataSource.getCurrentWeatherByCityName(any())).thenReturn(Single.just(currentWeather))

        val subscriber = viewModel.states().test()
        viewModel.processIntents(
                Observable.just(
                        CitiesViewIntent.InitialIntent
                )
        )

        subscriber.assertValues(
                CitiesViewState(isLoading = true, cityWeathers = emptyList(), error = null),
                CitiesViewState(isLoading = false, cityWeathers = listOf(Pair(city, weather)))
        )
    }

    @Test
    fun `天気の読み込みに失敗をしたとき`() {
        val city = City("東京", "tokyo")
        whenever(citiesDataSource.getCities()).thenReturn(Single.just(listOf(city)))
        val error = RuntimeException()
        whenever(weatherDataSource.getCurrentWeatherByCityName(any())).thenReturn(Single.error(error))

        val subscriber = viewModel.states().test()
        viewModel.processIntents(
                Observable.just(
                        CitiesViewIntent.InitialIntent
                )
        )

        subscriber.assertValues(
                CitiesViewState(isLoading = true, cityWeathers = emptyList(), error = null),
                CitiesViewState(isLoading = false, cityWeathers = listOf(Pair(city, null)))
        )
    }

    @Test
    fun `初期状態の state をテスト`() {
        val expected = CitiesViewState(isLoading = true, cityWeathers = emptyList())

        val subscriber = viewModel.states().test()
        viewModel.processIntents(Observable.just(CitiesViewIntent.InitialIntent))
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
        val city = City("東京", "tokyo")
        val result = CitiesViewResult.LoadCitiesResult.Success(CitiesViewResult.CityWeather(city = city, weather = null))
        val actual = CitiesViewModel.reducer.apply(CitiesViewState.idle(), result)
        val expected = CitiesViewState(false, listOf(Pair(city, null)))
        assert(actual).isEqualTo(expected)
    }

    @Test
    fun `読み込み失敗のreducer`() {
        val result = CitiesViewResult.LoadCitiesResult.Failure(Exception(""))
        val actual = CitiesViewModel.reducer.apply(CitiesViewState.idle(), result).isLoading
        assert(actual).isEqualTo(false)
    }

}