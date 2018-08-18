@file:Suppress("RemoveRedundantBackticks")

package net.numa08.mviweather.presentation.viewmodel

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import net.numa08.mviweather.data.City
import net.numa08.mviweather.data.source.WeatherDataSourece
import net.numa08.mviweather.presentation.intent.CityDetailViewIntent
import net.numa08.mviweather.presentation.state.CitiesViewState
import net.numa08.mviweather.presentation.state.CityDetailViewState
import net.numa08.mviweather.utils.TestSchedulerProvider
import net.numa08.openweathermaplib.models.common.Weather
import net.numa08.openweathermaplib.models.threehourforecast.ThreeHourForecast
import net.numa08.openweathermaplib.models.threehourforecast.ThreeHourWeather
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class CityDetailViewModelTest {

    @Mock
    private val dataSource: WeatherDataSourece = mock()

    private lateinit var viewModel: CityDetailViewModel

    @Before
    fun initViewModel() {
        viewModel = CityDetailViewModel(dataSource, TestSchedulerProvider())
    }

    @Test
    fun `読み込み中にエラーが発生した場合`() {
        val error = RuntimeException("error")
        val response = Single.error<ThreeHourForecast>(error)
        whenever(dataSource.getThreeHourForecastByCityName(any())).thenReturn(response)

        val subscriber = viewModel.states().test()
        val city = City("東京", "tokyo")
        viewModel.processIntents(
                Observable.just(
                        CityDetailViewIntent.InitialIntent(city)))
        subscriber.assertValues(
                CityDetailViewState.idle(),
                CityDetailViewState(city, forecast = null, error = null, isLoading = true),
                CityDetailViewState(city, forecast = null, error = error, isLoading = false)
        )
    }

    @Test
    fun `天気予報を読み込むことができた場合`() {
        val weather = Weather().also {
            it.description = "sunny"
            it.main = "sunny"
            it.id = 100
            it.icon = "sunny"
        }
        val threeHourWeather = ThreeHourWeather().also {
            it.weatherArray = arrayListOf(weather)
        }
        val cityForForecast = net.numa08.openweathermaplib.models.threehourforecast.City().also {
            it.name = "tokyo"
        }
        val forecast = ThreeHourForecast().also {
            it.city = cityForForecast
            it.threeHourWeatherArray = arrayListOf(threeHourWeather)
        }
        val response = Single.just(forecast)
        whenever(dataSource.getThreeHourForecastByCityName(any())).thenReturn(response)

        val subscriber = viewModel.states().test()
        val city = City("東京", "tokyo")
        viewModel.processIntents(
                Observable.just(
                        CityDetailViewIntent.InitialIntent(city)
                )
        )
        subscriber.assertValues(
                CityDetailViewState.idle(),
                CityDetailViewState(city, forecast = null, error = null, isLoading = true),
                CityDetailViewState(city, forecast = forecast, error = null, isLoading = false)
        )
    }

}