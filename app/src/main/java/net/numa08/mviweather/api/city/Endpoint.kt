package net.numa08.mviweather.api.city

import io.reactivex.Single
import net.numa08.mviweather.data.City
import retrofit2.http.GET

interface Endpoint {
    @GET("/mugifly/d6e68a516de4a008687c/raw/ae4795a7930a53804f67db9fd5284f6de4128a9c/japan-prefectures-roman-hepburn.csv")
    fun getCities(): Single<List<City>>
}