package net.numa08.mviweather.di.activitymodule

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.numa08.mviweather.presentation.activity.MainActivity

@Module
interface MainActivityBuilder {

    @ContributesAndroidInjector(modules = [
        MainActivityModule::class
    ])
    fun contributeMainActivity(): MainActivity

}