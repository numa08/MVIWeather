package net.numa08.mviweather.data.source.openwhathermap.remote

import io.reactivex.Single
import net.numa08.mviweather.data.HepburnName
import net.numa08.mviweather.data.source.WeatherDataSource
import net.numa08.openweathermaplib.implementation.OpenWeatherMapHelper
import net.numa08.openweathermaplib.models.currentweather.CurrentWeather
import net.numa08.openweathermaplib.models.threehourforecast.ThreeHourForecast

class RemoteOpenWeatherMapDataSource(private val helper: OpenWeatherMapHelper) : WeatherDataSource {

    override fun getCurrentWeatherByCityName(cityName: HepburnName): Single<CurrentWeather> = Single.create { source ->
        helper.getCurrentWeatherByCityName(cityName, object : OpenWeatherMapHelper.CurrentWeatherCallback {
            override fun onSuccess(weather: CurrentWeather) {
                source.onSuccess(weather)
            }

            override fun onFailure(err: Throwable) {
                source.onError(err)
            }
        })
    }

    override fun getThreeHourForecastByCityName(cityName: HepburnName): Single<ThreeHourForecast> = Single.create { source ->
        helper.getThreeHourForecastByCityName(cityName, object : OpenWeatherMapHelper.ThreeHourForecastCallback {
            override fun onSuccess(forecast: ThreeHourForecast) {
                source.onSuccess(forecast)
            }

            override fun onFailure(err: Throwable) {
                source.onError(err)
            }
        })
    }
}