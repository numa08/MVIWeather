package net.numa08.mviweather.presentation.activity

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import net.numa08.mviweather.R
import net.numa08.mviweather.presentation.fragment.CitiesFragment
import net.numa08.mviweather.utils.addFragmentToActivity

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (supportFragmentManager.findFragmentById(R.id.content_fragment) == null) {
            addFragmentToActivity(supportFragmentManager, CitiesFragment(), R.id.content_fragment)
        }
    }

}
