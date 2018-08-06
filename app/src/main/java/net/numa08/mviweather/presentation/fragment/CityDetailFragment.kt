package net.numa08.mviweather.presentation.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import net.numa08.mviweather.R
import net.numa08.mviweather.data.City
import net.numa08.mviweather.mvibase.MviView
import net.numa08.mviweather.presentation.intent.CityDetailViewIntent
import net.numa08.mviweather.presentation.state.CityDetailViewState

class CityDetailFragment : Fragment(), MviView<CityDetailViewIntent, CityDetailViewState> {

    companion object {
        private object Args {
            const val CITY_NAME = "city_name"
            const val CITY_HEPBURN_NAME = "city_hepburn_name"
        }

        fun instance(city: City): CityDetailFragment = CityDetailFragment().also { fragment ->
            val args = Bundle().also {
                it.putString(Args.CITY_NAME, city.name)
                it.putString(Args.CITY_HEPBURN_NAME, city.nameAsHepburn)
            }
            fragment.arguments = args
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_city_detail, container, false)

    override fun intents(): Observable<CityDetailViewIntent> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun render(state: CityDetailViewState) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}