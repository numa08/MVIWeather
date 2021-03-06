package net.numa08.mviweather.di.activitymodule

import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.numa08.mviweather.di.ViewModelKey
import net.numa08.mviweather.presentation.activity.MainActivity
import net.numa08.mviweather.presentation.fragment.CitiesFragment
import net.numa08.mviweather.presentation.fragment.CityDetailFragment
import net.numa08.mviweather.presentation.viewmodel.CitiesViewModel
import net.numa08.mviweather.presentation.viewmodel.CityDetailViewModel

@Module
interface MainActivityModule {

    @Binds
    fun providesAppCompatActivity(activity: MainActivity): AppCompatActivity

    @ContributesAndroidInjector
    fun contributeCitiesFragment(): CitiesFragment

    @ContributesAndroidInjector
    fun contributeCityDetailFragment(): CityDetailFragment

    @Binds
    @IntoMap
    @ViewModelKey(CitiesViewModel::class)
    fun bindCitiesViewModel(viewModel: CitiesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CityDetailViewModel::class)
    fun bindCityDetailViewModel(viewModel: CityDetailViewModel): ViewModel

}