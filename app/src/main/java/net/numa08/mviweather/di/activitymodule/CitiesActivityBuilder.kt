package net.numa08.mviweather.di.activitymodule

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.numa08.mviweather.presentation.activity.CitiesActivity

@Module
interface CitiesActivityBuilder {
    @ContributesAndroidInjector(modules = [
        CitiesActivityModule::class
    ])
    fun contributeCitiesActivity(): CitiesActivity

}