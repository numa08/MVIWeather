package net.numa08.mviweather.data.source

import io.reactivex.Single
import net.numa08.mviweather.data.City

interface CitiesDataSource {
    fun getCities(): Single<List<City>>
}