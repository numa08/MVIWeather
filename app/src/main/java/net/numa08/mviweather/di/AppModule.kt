package net.numa08.mviweather.di

import dagger.Module
import dagger.Provides
import net.numa08.mviweather.utils.AppSchedulerProvider
import net.numa08.mviweather.utils.SchedulerProvider
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton @Provides
    fun providesSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()

}