package net.numa08.mviweather.di

import dagger.Module
import dagger.Provides
import net.numa08.mviweather.presentation.AppNavigationController
import net.numa08.mviweather.presentation.NavigationController
import net.numa08.mviweather.utils.AppSchedulerProvider
import net.numa08.mviweather.utils.SchedulerProvider
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton @Provides
    fun providesSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()

    @Singleton
    @Provides
    fun providesNavigationController(schedulerProvider: SchedulerProvider): NavigationController = AppNavigationController(schedulerProvider)

}