package net.numa08.mviweather.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import net.numa08.mviweather.di.activitymodule.CitiesActivityBuilder
import net.numa08.mviweather.presentation.App
import javax.inject.Singleton

@Component(modules = [
    AndroidSupportInjectionModule::class,
    NetworkModule::class,
    ViewModelModule::class,
    CitiesActivityBuilder::class,
    AppModule::class
])
@Singleton
interface AppComponent: AndroidInjector<App>{

    @Component.Builder
    interface Builder {
        @BindsInstance fun application(app: App): Builder
        fun networkModule(module: NetworkModule): Builder
        fun build(): AppComponent
    }

    override fun inject(instance: App)
}