package net.numa08.mviweather.presentation.activity

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import net.numa08.mviweather.R
import net.numa08.mviweather.presentation.NavigationController
import net.numa08.mviweather.presentation.NavigationEvent
import net.numa08.mviweather.presentation.fragment.CitiesFragment
import net.numa08.mviweather.presentation.fragment.CityDetailFragment
import net.numa08.mviweather.utils.addFragmentToActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    companion object {
        const val MAIN_BACK_STACK = "main_back_stack"
    }

    @Inject
    lateinit var navigationController: NavigationController
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (supportFragmentManager.findFragmentById(R.id.content_fragment) == null) {
            addFragmentToActivity(supportFragmentManager,
                    CitiesFragment(), R.id.content_fragment, MAIN_BACK_STACK)
        }
        disposables.add(
                navigationController
                        .toNavigate()
                        .ofType(NavigationEvent.WeatherDetailForCity::class.java)
                        .map { CityDetailFragment.instance(it.city) }
                        .subscribe {
                            addFragmentToActivity(supportFragmentManager,
                                    it, R.id.content_fragment, MAIN_BACK_STACK)
                        }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}
