package net.numa08.mviweather.di

import dagger.Module
import dagger.Provides
import net.numa08.mviweather.api.city.Endpoint
import net.numa08.mviweather.utils.parser.CitiesCSVParser
import net.numa08.mviweather.utils.parser.CitiesResponseBodyConverter
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

// テストで再利用をするため open class とする
@Module
open class NetworkModule {

    companion object {
        val instance = NetworkModule()
    }

    @Singleton @Provides
    fun provideOkHttpClient(): OkHttpClient =
            OkHttpClient.Builder().build()

    @RetrofitForCities @Singleton @Provides
    fun provideRetrofitForCities(okHttpClient: OkHttpClient): Retrofit =
            Retrofit
                    .Builder()
                    .client(okHttpClient)
                    .baseUrl("https://gist.githubusercontent.com/")
                    .addConverterFactory(CitiesResponseBodyConverter.Factory.create(CitiesCSVParser()))
                    .build()

    @Singleton @Provides
    open fun provideCitiesEndpoint(@RetrofitForCities retrofit: Retrofit): Endpoint = retrofit.create(Endpoint::class.java)
}