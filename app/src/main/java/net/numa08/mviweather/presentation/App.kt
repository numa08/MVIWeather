package net.numa08.mviweather.presentation

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import net.numa08.mviweather.di.DaggerAppComponent
import net.numa08.mviweather.di.NetworkModule

open class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
            DaggerAppComponent.builder()
                    .application(this)
                    .networkModule(NetworkModule.instance)
                    .build()

}