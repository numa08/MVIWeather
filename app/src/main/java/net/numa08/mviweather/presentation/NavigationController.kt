package net.numa08.mviweather.presentation

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import net.numa08.mviweather.data.City
import net.numa08.mviweather.utils.SchedulerProvider

interface NavigationController {
    fun navigateTo(event: NavigationEvent)
    fun toNavigate(): Observable<NavigationEvent>
}

sealed class NavigationEvent {
    class WeatherDetailForCity(val city: City) : NavigationEvent()
}

class AppNavigationController constructor(
        private val schedulerProvider: SchedulerProvider
) : NavigationController {

    private val subject = PublishSubject.create<NavigationEvent>()

    override fun navigateTo(event: NavigationEvent) {
        subject.onNext(event)
    }

    override fun toNavigate(): Observable<NavigationEvent> = subject.observeOn(schedulerProvider.ui())

}