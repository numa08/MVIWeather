package net.numa08.mviweather.utils

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class TestSchedulerProvider: SchedulerProvider {
    override fun ui(): Scheduler = Schedulers.trampoline()

    override fun computation(): Scheduler = Schedulers.trampoline()

    override fun newThread(): Scheduler = Schedulers.trampoline()

    override fun io(): Scheduler = Schedulers.trampoline()
}