package net.numa08.mviweather.data.source

import io.reactivex.Single
import net.numa08.mviweather.data.HepburnName
import net.numa08.openweathermaplib.models.currentweather.CurrentWeather
import net.numa08.openweathermaplib.models.threehourforecast.ThreeHourForecast

interface WeatherDataSourece {
    fun getCurrentWeatherByCityName(cityName: HepburnName): Single<CurrentWeather>
    fun getThreeHourForecastByCityName(cityName: HepburnName): Single<ThreeHourForecast>
}