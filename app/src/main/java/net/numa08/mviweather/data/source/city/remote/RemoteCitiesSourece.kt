package net.numa08.mviweather.data.source.city.remote

import io.reactivex.Single
import net.numa08.mviweather.api.city.Endpoint
import net.numa08.mviweather.data.City
import net.numa08.mviweather.data.source.CitiesDataSource
import javax.inject.Inject

class RemoteCitiesSourece @Inject constructor(
        private val endpoint: Endpoint
) : CitiesDataSource {
    override fun getCities(): Single<List<City>> = endpoint.getCities()
}