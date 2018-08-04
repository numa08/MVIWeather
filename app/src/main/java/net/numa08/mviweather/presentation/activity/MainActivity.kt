package net.numa08.mviweather.presentation.activity

import android.os.Bundle
import android.util.Log
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import net.numa08.mviweather.R
import net.numa08.mviweather.presentation.NavigationController
import net.numa08.mviweather.presentation.NavigationEvent
import net.numa08.mviweather.presentation.fragment.CitiesFragment
import net.numa08.mviweather.utils.addFragmentToActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var navigationController: NavigationController
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (supportFragmentManager.findFragmentById(R.id.content_fragment) == null) {
            addFragmentToActivity(supportFragmentManager, CitiesFragment(), R.id.content_fragment)
        }
        disposables.add(
                navigationController
                        .toNavigate()
                        .ofType(NavigationEvent.WeatherDetailForCity::class.java)
                        .subscribe {
                            Log.d("test", it.name)
                        }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}
