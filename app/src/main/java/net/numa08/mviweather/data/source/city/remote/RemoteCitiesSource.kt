package net.numa08.mviweather.data.source.city.remote

import io.reactivex.Single
import net.numa08.mviweather.api.city.Endpoint
import net.numa08.mviweather.data.City
import net.numa08.mviweather.data.source.CitiesDataSource

class RemoteCitiesSource(
        private val endpoint: Endpoint
) : CitiesDataSource {
    override fun getCities(): Single<List<City>> = endpoint.getCities()
}