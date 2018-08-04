package net.numa08.mviweather.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import net.numa08.mviweather.data.City

class CitiesAdapter(context: Context) : ArrayAdapter<City>(context, 0) {

    private val citySelectedSubject = PublishSubject.create<City>()
    val citySelectedObserver: Observable<City>
        get() = citySelectedSubject

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView
                ?: LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, null)
        val city = getItem(position)
        view.findViewById<TextView>(android.R.id.text1).text = city.name
        view.setOnClickListener { citySelectedSubject.onNext(city) }
        return view
    }

}