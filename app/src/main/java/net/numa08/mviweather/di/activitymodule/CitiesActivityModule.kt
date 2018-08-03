package net.numa08.mviweather.di.activitymodule

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.numa08.mviweather.di.ViewModelKey
import net.numa08.mviweather.presentation.viewmodel.CitiesViewModel

@Module
interface CitiesActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(CitiesViewModel::class)
    fun bindViewModel(viewModel: CitiesViewModel): ViewModel

}